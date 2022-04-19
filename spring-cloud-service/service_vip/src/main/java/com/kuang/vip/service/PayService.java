package com.kuang.vip.service;

import com.kuang.vip.entity.AlipayBean;

import java.util.Map;

/*支付服务*/
public interface PayService {

    /*支付宝*/
    String aliPay(AlipayBean alipayBean);

    //支付宝支付异步通知执行方法
    String notify(Map<String, String> conversionParams);

    //用户主动查询支付状态
    void checkAlipay(String outTradeNo);

    //充值k币，生成支付账单
    String addOrder(String id, String userId);
}
