package com.kuang.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.bbs.client.VipClient;
import com.kuang.bbs.entity.Column;
import com.kuang.bbs.entity.ColumnAuthor;
import com.kuang.bbs.entity.vo.ColumnArticleVo;
import com.kuang.bbs.entity.vo.ColumnAuthorVo;
import com.kuang.bbs.entity.vo.ColumnDetailVo;
import com.kuang.bbs.entity.vo.ColumnVo;
import com.kuang.bbs.mapper.ColumnAuthorMapper;
import com.kuang.bbs.mapper.ColumnMapper;
import com.kuang.bbs.service.ColumnAuthorService;
import com.kuang.bbs.service.ColumnService;
import com.kuang.bbs.utils.ColumnUtils;
import com.kuang.springcloud.entity.RightRedis;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.springcloud.utils.VipUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-03-24
 */
@Service
public class ColumnServiceImpl extends ServiceImpl<ColumnMapper, Column> implements ColumnService {

    @Resource
    private VipClient vipClient;

    @Resource
    private ColumnAuthorMapper columnAuthorMapper;

    @Resource
    private ColumnAuthorService columnAuthorService;

    //创建专栏
    @Transactional
    @Override
    public Column addUserColumn(String userId, String nickname , String avatar , String title) {
        //校验是否可以增加专栏
        RightRedis userRightRedis = VipUtils.getUserRightRedis(userId);
        if(userRightRedis == null){
            R rightRedisByUserId = vipClient.findRightRedisByUserId(userId);
            if(!rightRedisByUserId.getSuccess()){
                throw new XiaoXiaException(ResultCode.ERROR , "创建专栏失败");
            }
            userRightRedis = (RightRedis) rightRedisByUserId.getData().get("rightRedis");
        }

        QueryWrapper<Column> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        Integer integer = baseMapper.selectCount(wrapper);
        if(integer + 1 > userRightRedis.getColumnNumber()){
            throw new XiaoXiaException(ResultCode.ERROR , "专栏数量过多，不能创建");
        }

        //插入专栏
        Column column = new Column();
        column.setUserId(userId);
        column.setTitle(title);
        int insert = baseMapper.insert(column);
        if(insert != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "创建失败");
        }

        //插入专栏作者
        ColumnAuthor columnAuthor = new ColumnAuthor();
        columnAuthor.setColumnId(column.getId());
        columnAuthor.setUserId(userId);
        columnAuthor.setAvatar(avatar);
        columnAuthor.setNickname(nickname);
        int insert1 = columnAuthorMapper.insert(columnAuthor);
        if(insert1 != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "创建失败");
        }
        return column;
    }

    //查看用户专栏数量
    @Override
    public Integer findUserColumnNumber(String userId) {
        QueryWrapper<Column> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }

    //查看其他用户专栏数量
    @Override
    public Integer findOtherUserColumnNumber(String userId) {
        QueryWrapper<Column> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.eq("is_release" , 1);
        return baseMapper.selectCount(wrapper);
    }

    //查询用户专栏
    @Override
    public List<ColumnVo> findUserColumn(String userId) {
        return baseMapper.findUserColumn(userId);
    }

    //查询他人专栏
    @Override
    public List<ColumnVo> findOtherUserColumn(String userId) {
        return baseMapper.findOtherUserColumn(userId);
    }

    //校验用户是否可以访问
    @Override
    public Future<Boolean> checkUserAbility(String userId, String columnId) {
        Column column = baseMapper.selectById(columnId);
        //专栏不存在
        if(column == null){
            return new AsyncResult<>(false);
        }

        //如果访问者就是创建者,直接返回
        if(column.getUserId().equals(userId)){
            return new AsyncResult<>(true);
        }else {
            //下面访问者不是创建者
            //专栏是否被外部可见
            if(!column.getIsRelease()){
                return new AsyncResult<>(false);
            }
            //是否每个人都可见
            if(column.getVsibility() == ColumnUtils.EVERYONE){
                return new AsyncResult<>(true);
            }

            //如果仅仅自己可见
            if(column.getVsibility() == ColumnUtils.OWN){
                return new AsyncResult<>(false);
            }

            //取出用户权益
            RightRedis userRightRedis = VipUtils.getUserRightRedis(userId);
            if(userRightRedis == null){
                R rightRedisByUserId = vipClient.findRightRedisByUserId(userId);
                if(!rightRedisByUserId.getSuccess()){
                    return new AsyncResult<>(false);
                }
                userRightRedis = (RightRedis) rightRedisByUserId.getData().get("rightRedis");
            }

            //看是否符合会员权益
            if(column.getVsibility() == ColumnUtils.VIPMEMBER){
                boolean b = ColumnUtils.checkAllVipRight(userRightRedis.getId());
                return new AsyncResult<>(b);
            }

            //看是否符合年会员
            if(column.getVsibility() == ColumnUtils.YEARVIPMEMBER){
                boolean b = ColumnUtils.checkYearVipRight(userRightRedis.getId());
                return new AsyncResult<>(b);
            }

            //看是否符合终身会员
            if(column.getVsibility() == ColumnUtils.LOFELONGVIPMEMBER){
                boolean b = ColumnUtils.checkLifeLongVipRight(userRightRedis.getId());
                return new AsyncResult<>(b);
            }
        }

        return new AsyncResult<>(false);
    }

    //查询专栏具体数据
    @Override
    public ColumnDetailVo findColumnDetail(String columnId) {
        Column column = baseMapper.selectById(columnId);
        ColumnDetailVo columnDetailVo = new ColumnDetailVo();
        BeanUtils.copyProperties(column , columnDetailVo);
        columnDetailVo.setColumnId(column.getId());
        List<ColumnAuthorVo> columnAuthorVoList = columnAuthorService.findColumnAuthorList(columnId);
        columnDetailVo.setAuthorList(columnAuthorVoList);
        return columnDetailVo;
    }
}
