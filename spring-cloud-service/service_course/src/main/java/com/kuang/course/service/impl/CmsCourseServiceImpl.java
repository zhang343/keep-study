package com.kuang.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import com.kuang.course.service.CmsVideoService;
import com.kuang.springcloud.entity.BbsCourseVo;
import com.kuang.springcloud.entity.InfoMyNewsVo;
import com.kuang.springcloud.entity.MessageCourseVo;
import com.kuang.springcloud.entity.RightRedis;
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
        return baseMapper.findCourseByTcId(tcId);
    }

    //查找课程详细信息
    @Override
    public CourseVo findCourseVo(String courseId) {
        CourseVo courseVo = baseMapper.findCourseVo(courseId);
        if(courseVo == null){
            throw new XiaoXiaException(ResultCode.ERROR , "该课程不存在");
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
        //查询是否有这个课程和该课程的信息
        CmsCourse course = baseMapper.selectById(courseId);
        if(course == null || course.getPrice() == 0){
            throw new XiaoXiaException(ResultCode.ERROR , "该课程不存在或该课程价格为0");
        }
        //查询这个课程是否已经被用户购买
        boolean flag = billService.findBillByCourseIdAndUserId(courseId, userId);
        if(flag){
            throw new XiaoXiaException(ResultCode.ERROR , "请勿重复购买课程");
        }
        //在账单中插入一条数据，表示用户购买
        CmsBill bill = new CmsBill();
        bill.setCourseId(courseId);
        bill.setUserId(userId);
        int insert = billMapper.insert(bill);
        if(insert != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "课程购买失败");
        }
        //用户对应课程打折数量
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

    //为消息模块服务，查询课程
    @Override
    public List<MessageCourseVo> findMessageCourseVo(List<String> courseIdList) {
        List<MessageCourseVo> messageCourseVos = baseMapper.findMessageCourseVo(courseIdList);
        for(MessageCourseVo messageCourseVo : messageCourseVos){
            Integer videoNumberByCourseId = videoService.findVideoNumberByCourseId(messageCourseVo.getCourseId());
            messageCourseVo.setVideoNumber(videoNumberByCourseId);
        }
        return messageCourseVos;
    }

    //查找课程播放量
    @Override
    public List<CmsCourse> findCourseViewsList(List<String> courseIdList) {
        QueryWrapper<CmsCourse> wrapper = new QueryWrapper<>();
        wrapper.select("id" , "views");
        wrapper.in("id" , courseIdList);
        return baseMapper.selectList(wrapper);
    }

    //用户购买课程成功，发送消息到rabbitmq
    @Async
    @Override
    public void sendMyNews(String userId, String courseId, String title) {
        InfoMyNewsVo infoMyNewsVo = new InfoMyNewsVo();
        infoMyNewsVo.setUserId(userId);
        infoMyNewsVo.setIsCourse(true);
        infoMyNewsVo.setCourseTitle(title);
        infoMyNewsVo.setCourseId(courseId);
        msgProducer.sendMyNews(JSON.toJSONString(infoMyNewsVo));
    }

    //更新课程浏览量
    @Override
    public void updateCourseViews(List<CmsCourse> courseList) {
        baseMapper.updateCourseViews(courseList);
    }


}
