package com.kuang.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.course.client.UcenterClient;
import com.kuang.course.client.VipClient;
import com.kuang.course.entity.CmsBill;
import com.kuang.course.entity.CmsCourse;
import com.kuang.course.entity.vo.CourseVo;
import com.kuang.course.entity.vo.IndexCourseVo;
import com.kuang.course.mapper.CmsBillMapper;
import com.kuang.course.mapper.CmsCourseMapper;
import com.kuang.course.service.CmsBillService;
import com.kuang.course.service.CmsCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.course.service.CmsVideoService;
import com.kuang.springcloud.entity.BbsCourseVo;
import com.kuang.springcloud.entity.MessageCourseVo;
import com.kuang.springcloud.entity.RightRedis;
import com.kuang.springcloud.entity.UserStudyVo;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.rabbitmq.MsgProducer;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.RedisUtils;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.springcloud.utils.VipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
@Service
@Slf4j
public class CmsCourseServiceImpl extends ServiceImpl<CmsCourseMapper, CmsCourse> implements CmsCourseService {

    @Resource
    private CmsBillMapper billMapper;

    @Resource
    private CmsBillService billService;

    @Resource
    private CmsVideoService videoService;

    @Resource
    private VipClient vipClient;

    @Resource
    private UcenterClient ucenterClient;

    @Resource
    private MsgProducer msgProducer;

    //通过二级分类id查找课程
    @Cacheable(value = "indexCourseVoList")
    @Override
    public List<IndexCourseVo> findCourseByTcId(String tcId) {
        log.info("通过二级分类id查找下属课程,二级分类id:" + tcId);
        return baseMapper.findCourseByTcId(tcId);
    }

    //查找课程详细信息
    @Override
    public CourseVo findCourseVo(String courseId) {
        log.info("通过课程id查找课程详细信息,课程id:" + courseId);
        CourseVo courseVo = baseMapper.findCourseVo(courseId);
        if(courseVo == null){
            log.warn("有人进行非法操作，查询不存在课程,课程id:" + courseId);
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        //查询课程对应小节数量
        Integer videoNumber = videoService.findVideoNumberByCourseId(courseId);
        courseVo.setVideoNumber(videoNumber);
        return courseVo;
    }

    //用户购买课程,并且返回课程标题
    @Transactional
    @Override
    public String buyCourse(String courseId, String userId) {
        log.info("用户购买课程,课程id:" + courseId + ",用户id:" + userId);
        //查询是否有这个课程
        QueryWrapper<CmsCourse> wrapper = new QueryWrapper<>();
        wrapper.select("price" , "title");
        wrapper.eq("id" , courseId);
        CmsCourse course = baseMapper.selectOne(wrapper);
        if(course == null || course.getPrice() == 0){
            throw new XiaoXiaException(ResultCode.ERROR , "购买课程失败");
        }
        //查询这个课程是否已经被用户购买
        boolean flag = billService.findBillByCourseIdAndUserId(courseId, userId);
        //判断课程是否存在，如果存在，看价格是否为0，再看这个课程是否已经被用户购买
        if(flag){
            log.warn("有人进行非法购买课程操作,课程id:" + courseId);
            throw new XiaoXiaException(ResultCode.ERROR , "购买课程失败");
        }
        //在账单中插入一条数据，表示用户购买
        CmsBill bill = new CmsBill();
        bill.setCourseId(courseId);
        bill.setUserId(userId);
        int insert = billMapper.insert(bill);
        if(insert != 1){
            log.error("用户购买课程失败,课程id:" + courseId + ",用户id:" + userId);
            throw new XiaoXiaException(ResultCode.ERROR , "课程购买失败");
        }
        //远程调用获取用户对应课程打折数量
        log.info("开始进行远程调用,查出用户对应vip等级的打折数,用户id:" + userId);
        RightRedis userRightRedis = VipUtils.getUserRightRedis(userId);
        if(userRightRedis == null){
            R rightRedisByUserId = vipClient.findRightRedisByUserId(userId);
            if(!rightRedisByUserId.getSuccess()){
                throw new XiaoXiaException(ResultCode.ERROR , "课程购买失败");
            }
            userRightRedis = (RightRedis) rightRedisByUserId.getData().get("rightRedis");
        }

        double courseDiscount = userRightRedis.getCourseDiscount();
        //对课程进行相应打折
        int price = (int) (course.getPrice() * courseDiscount);
        if(price != 0){
            //减去用户k币
            log.info("开始远程调用service-ucenter下面的接口/KCoin/reduce,减去用户k币:" + price);
            R ucenterR = ucenterClient.reduce(price);
            if(!ucenterR.getSuccess()){
                log.error("远程调用service-ucenter下面的接口/KCoin/reduce失败,未减去用户k币");
                throw new XiaoXiaException(ResultCode.ERROR , "你的k币不足");
            }
        }

        return course.getTitle();
    }

    //查找价格为前三的课程
    @Override
    public List<BbsCourseVo> findCourseOrderByPrice() {
        Object value = RedisUtils.getValue(RedisUtils.COURSEORDERBYPRICE);
        if(value != null){
            return (List<BbsCourseVo>) value;
        }
        List<BbsCourseVo> courseOrderByPrice = baseMapper.findCourseOrderByPrice();
        RedisUtils.setValueTimeout(RedisUtils.COURSEORDERBYPRICE , courseOrderByPrice , 120);
        return courseOrderByPrice;
    }

    //向用户历史记录发送消息
    @Async
    @Override
    public void sendHistoryMsg(String userId, String videoId) {
        UserStudyVo userStudyVoByCourseId = videoService.findUserStudyVoByCourseId(videoId);
        userStudyVoByCourseId.setUserId(userId);
        msgProducer.sendHistoryMsg(JSON.toJSONString(userStudyVoByCourseId));
    }

    //更新课程浏览量
    @Override
    public void updateCourseViews(List<CmsCourse> courseList) {
        baseMapper.updateCourseViews(courseList);
    }

    //为消息模块服务，查询课程
    @Override
    public List<MessageCourseVo> findMessageCourseVo(List<String> courseIdList) {
        List<MessageCourseVo> messageCourseVos = baseMapper.findMessageCourseVo(courseIdList);
        for(MessageCourseVo messageCourseVo : messageCourseVos){
            Integer videoNumberByCourseId = videoService.findVideoNumberByCourseId(messageCourseVo.getId());
            messageCourseVo.setVideoNumber(videoNumberByCourseId);
        }
        return messageCourseVos;
    }


}
