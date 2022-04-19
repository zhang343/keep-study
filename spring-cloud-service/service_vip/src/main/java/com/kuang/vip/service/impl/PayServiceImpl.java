package com.kuang.vip.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.RedisUtils;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.vip.client.UcenterClient;
import com.kuang.vip.config.AliPayConfig;
import com.kuang.vip.entity.AlipayBean;
import com.kuang.vip.entity.MoneyK;
import com.kuang.vip.entity.Order;
import com.kuang.vip.mapper.MoneyKMapper;
import com.kuang.vip.mapper.OrderMapper;
import com.kuang.vip.service.PayService;
import com.kuang.vip.utils.AlipayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Resource
    private MoneyKMapper moneyKMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UcenterClient ucenterClient;

    @Override
    public String aliPay(AlipayBean alipayBean) {
        String url = null;
        try {
            url = AlipayUtil.connect(alipayBean);
        } catch(AlipayApiException e) {
            throw new XiaoXiaException(ResultCode.ERROR, "请求失败");
        }
        return url;
    }


    //三种情况
    /*
    支付宝调用：支付成功，数据库中存在一条未修改支付状态成功的的数据，加k币
    用户查询：
    1：支付成功，但是账单是以前支付成功的，已经处理完了，账单是处于支付成功的状态
    2：支付成功，账单是刚才支付成功，数据库中存在一条未修改支付状态成功的的数据，加k币
     */
    @Transactional
    public void updateOrderAndMembers(String orderId, Boolean isUserFind) {
        log.info("开始查询数据库订单的支付状态,订单号:" + orderId);
        Order order = orderMapper.selectById(orderId);
        if(order.getStatus()){
            log.info("查询数据库订单的支付状态为支付成功，说明已经操作完毕,订单号:" + orderId);
            return;
        }

        log.info("开始去获取分布式锁,订单号:" + orderId);
        boolean b = RedisUtils.tryOrderLock(orderId, 180);
        if(b){
            log.info("获取分布式锁成功,订单号:" + orderId);
            //更新订单支付状态
            Order orderUpdate = new Order();
            orderUpdate.setId(orderId);
            orderUpdate.setStatus(true);
            int i = orderMapper.updateById(orderUpdate);
            if(i != 1){
                RedisUtils.unOrderLock(orderId);
                throw new RuntimeException();
            }

            //加k币
            R add = ucenterClient.add(order.getPrice() , order.getUserId());
            if(!add.getSuccess()){
                RedisUtils.unOrderLock(orderId);
                throw new RuntimeException();
            }

            RedisUtils.unOrderLock(orderId);
        }else {
            log.info("获取分布式锁失败,订单号:" + orderId);
            throw new RuntimeException();
        }
    }

    //支付宝支付异步通知执行方法
    @Override
    public String notify(Map<String, String> conversionParams) {
        log.info("支付宝异步请求逻辑处理notify方法");

        //该接口可能会被外部用户恶意调用，或者说该接口确实被支付宝调用，所以我们需要进行数据验证
        //签名验证(对支付宝返回的数据验证，确定是支付宝返回的)
        log.info("支付宝异步请求逻辑处理notify方法开始进行签名验证");
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(conversionParams, AliPayConfig.ALIPAYPUBLICKEY, AliPayConfig.CHARSET, AliPayConfig.SIGNTYPE); //调用SDK验证签名
        } catch(AlipayApiException e) {
            log.warn("支付宝异步请求逻辑处理notify方法签名验证失败，请注意");
        }


        if(signVerified) {
            //验签通过
            log.info("支付宝异步请求逻辑处理notify方法签名验证成功");
            //获取需要保存的数据
            String appId = conversionParams.get("app_id");//支付宝分配给开发者的应用Id
            String notifyTime = conversionParams.get("notify_time");//通知时间:yyyy-MM-dd HH:mm:ss
            String gmtCreate = conversionParams.get("gmt_create");//交易创建时间:yyyy-MM-dd HH:mm:ss
            String gmtPayment = conversionParams.get("gmt_payment");//交易付款时间
            String gmtRefund = conversionParams.get("gmt_refund");//交易退款时间
            String gmtClose = conversionParams.get("gmt_close");//交易结束时间
            String tradeNo = conversionParams.get("trade_no");//支付宝的交易号
            String outTradeNo = conversionParams.get("out_trade_no");//获取商户之前传给支付宝的订单号（商户系统的唯一订单号）
            String outBizNo = conversionParams.get("out_biz_no");//商户业务号(商户业务ID，主要是退款通知中返回退款申请的流水号)
            String buyerLogonId = conversionParams.get("buyer_logon_id");//买家支付宝账号
            String sellerId = conversionParams.get("seller_id");//卖家支付宝用户号
            String sellerEmail = conversionParams.get("seller_email");//卖家支付宝账号
            String totalAmount = conversionParams.get("total_amount");//订单金额:本次交易支付的订单金额，单位为人民币（元）
            String receiptAmount = conversionParams.get("receipt_amount");//实收金额:商家在交易中实际收到的款项，单位为元
            String invoiceAmount = conversionParams.get("invoice_amount");//开票金额:用户在交易中支付的可开发票的金额
            String buyerPayAmount = conversionParams.get("buyer_pay_amount");//付款金额:用户在交易中支付的金额
            String tradeStatus = conversionParams.get("trade_status");// 获取交易状态

            //再次验证，防止错误
            //支付宝官方建议校验的值（out_trade_no、total_amount、sellerId、app_id）
            log.info("开始进行校验out_trade_no、total_amount、app_id的值");
            Order order = orderMapper.selectById(outTradeNo);
            if(order != null && totalAmount.equals(order.getPaymentPrice().toString() + ".00") && AliPayConfig.APPID.equals(appId)) {
                log.info("校验out_trade_no、total_amount、app_id的值成功");
                log.info("开始判断订单支付状态");
                if(tradeStatus.equals("TRADE_SUCCESS")) {
                    //只处理支付成功的订单: 修改交易表状态,支付成功
                    log.info("订单支付支付成功,账单号为:" + outTradeNo);
                    String flag = "success";
                    try {
                        updateOrderAndMembers(order.getId() , false);
                        log.info("修改订单支付状态成功,账单号为:" + outTradeNo);
                    }catch(Exception e){
                        log.warn("修改订单支付状态失败,账单号为:" + outTradeNo);
                        flag = "failure";
                    }
                    return flag;
                } else {
                    log.warn("订单支付支付失败,账单号为:" + outTradeNo);
                    return "failure";
                }
            } else {
                log.warn("校验out_trade_no、total_amount、app_id的值失败，请注意");
                return "failure";
            }
        }
        return "failure";
    }

    //用户主动查询支付状态
    @Override
    public void checkAlipay(String outTradeNo) {

        log.info("向支付宝发起查询，查询商户订单号为:" + outTradeNo);
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                AliPayConfig.GATEWAYURL,//支付宝网关
                AliPayConfig.APPID,//appid
                AliPayConfig.MERCHANTPRIVATEKEY,//商户私钥
                "json",
                AliPayConfig.CHARSET,//字符编码格式
                AliPayConfig.ALIPAYPUBLICKEY,//支付宝公钥
                AliPayConfig.SIGNTYPE//签名方式
        );
        //设置请求参数
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest ();
        alipayRequest.setBizContent("{" +
                "\"out_trade_no\":\"" + outTradeNo + "\"" +
                "}");
        AlipayTradeQueryResponse alipayTradeQueryResponse = null;

        //查询
        try {
            alipayTradeQueryResponse = alipayClient.execute(alipayRequest);
        } catch(AlipayApiException e) {
            throw new XiaoXiaException(ResultCode.ERROR , "查询支付状态失败");
        }


        //查询成功
        if(alipayTradeQueryResponse.isSuccess()) {
            log.info("向支付宝发起查询成功，查询商户订单号为:" + outTradeNo);
            Order order = orderMapper.selectById(outTradeNo);
            if(order == null) {
                log.warn("有用户开始进行非法查询，请检查，订单号为:" + outTradeNo);
                throw new XiaoXiaException(ResultCode.ERROR, "查询失败");
            }

            String tradeStatus = alipayTradeQueryResponse.getTradeStatus();
            if(tradeStatus.equals("TRADE_SUCCESS")) {
                //支付成功
                log.info("用户支付成功，查询商户订单号为:" + outTradeNo);
                try {
                    updateOrderAndMembers(outTradeNo, true);
                    log.info("修改订单支付状态成功,账单号为:" + outTradeNo);
                } catch(Exception e) {
                    log.info("修改订单支付状态失败,账单号为:" + outTradeNo);
                    throw new XiaoXiaException(ResultCode.ERROR, "查询失败");
                }
            } else {
                //支付失败
                log.info("用户支付失败，查询商户订单号为:" + outTradeNo);
                throw new XiaoXiaException(ResultCode.ERROR, "支付失败");
            }
        }
    }

    //充值k币，生成支付账单
    @Transactional
    @Override
    public String addOrder(String id, String userId) {
        MoneyK moneyK = moneyKMapper.selectById(id);
        if(moneyK == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        Order order = new Order();
        order.setUserId(userId);
        order.setPaymentPrice(moneyK.getMoney());
        order.setPrice(moneyK.getPrice());
        int insert = orderMapper.insert(order);
        if(insert != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "与支付宝交互失败");
        }
        String s = aliPay(new AlipayBean()
                .setBody("充值k币")
                .setOut_trade_no(order.getId())
                .setTotal_amount(new StringBuffer().append(order.getPaymentPrice()))
                .setSubject("网站k币充值,充值k币数量为" + moneyK.getPrice()));
        return s;
    }
}
