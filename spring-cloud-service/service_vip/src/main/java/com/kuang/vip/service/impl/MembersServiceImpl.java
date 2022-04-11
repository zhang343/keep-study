package com.kuang.vip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.RedisUtils;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.springcloud.utils.VipUtils;
import com.kuang.vip.entity.AlipayBean;
import com.kuang.vip.entity.Members;
import com.kuang.vip.entity.Order;
import com.kuang.vip.entity.Rights;
import com.kuang.vip.mapper.MembersMapper;
import com.kuang.vip.mapper.OrderMapper;
import com.kuang.vip.mapper.RightsMapper;
import com.kuang.vip.service.CacheService;
import com.kuang.vip.service.MembersService;
import com.kuang.vip.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
@Slf4j
public class MembersServiceImpl extends ServiceImpl<MembersMapper, Members> implements MembersService {

    @Resource
    private RightsMapper rightsMapper;

    @Resource
    private CacheService cacheService;

    @Resource
    private PayService payService;

    @Resource
    private OrderMapper orderMapper;

    //用户充值vip
    @Transactional
    @Override
    public String addMember(String vipId , String userId) {

        //检查该vip是否存在
        Rights rights = rightsMapper.selectById(vipId);
        if(rights == null || rights.getPrice() == 0){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        //检查该用户是否存在
        QueryWrapper<Members> membersWrapper = new QueryWrapper<>();
        membersWrapper.eq("user_id" , userId);
        Integer integer = baseMapper.selectCount(membersWrapper);
        if(integer != 0){
            throw new XiaoXiaException(ResultCode.ERROR , "你已经是vip的,不要充值");
        }
        String s= null;
        //到此用户目前就不是vip,但是可能出现并发，此时多个线程到了这里，要求生成订单，利用分布式锁，保证只有一个线程会被执行，生成订单，其它失败
        boolean b = RedisUtils.tryVipLock(userId, 60);
        if(b){
            //此时只有一个线程进入到了这里，其他都被拒绝
            //先进行查询之前是否有账单未支付，且尚未过期
            Object value = RedisUtils.getValue(userId + "order");
            if(value != null){
                //有账单尚未支付，且未过期
                Order order = (Order) value;
                //发起支付宝请求
                s = payService.aliPay(new AlipayBean()
                        .setBody("商品名称为:" + order.getRightId())
                        .setOut_trade_no(order.getId())
                        .setTotal_amount(new StringBuffer().append(order.getPaymentPrice()))
                        .setSubject("网站会员充值,如果想变更充值,请该支付超时，然后可以关闭此页面"));
                RedisUtils.setValueTimeout(userId + "order" , order , 2 * 60);
            }else {
                //生成账单
                Order order = new Order();
                order.setUserId(userId);
                order.setRightId(vipId);
                order.setPaymentPrice(rights.getPrice());
                int insert = orderMapper.insert(order);
                if(insert != 1) {
                    RedisUtils.unVipLock(userId);
                    throw new XiaoXiaException(ResultCode.ERROR, "后台开小差了，请稍后在试");
                }
                //发起支付宝请求
                s = payService.aliPay(new AlipayBean()
                        .setBody("商品名称为:" + order.getRightId())
                        .setOut_trade_no(order.getId())
                        .setTotal_amount(new StringBuffer().append(order.getPaymentPrice()))
                        .setSubject("网站会员充值,如果想变更充值,请该支付超时，然后可以关闭此页面"));
                RedisUtils.setValueTimeout(userId + "order" , order , 2 * 60);
            }
            RedisUtils.unVipLock(userId);
        }else {
            throw new XiaoXiaException(ResultCode.ERROR , "请不要频繁操作");
        }

        return s;
    }


    //查询用户的viplogo
    @Override
    public Object findUserVipLevel(List<String> userIdList) {
        return VipUtils.getUserVipLevel(userIdList,
                cacheService.CacheAllMembersRedisTreeMap(),
                cacheService.CacheAllRightRedisTreeMap(),
                cacheService.CacheNotVipRight());
    }



}
