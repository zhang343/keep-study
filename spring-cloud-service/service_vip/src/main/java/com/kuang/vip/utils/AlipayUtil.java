package com.kuang.vip.utils;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.kuang.vip.config.AliPayConfig;
import com.kuang.vip.entity.AlipayBean;

/* 支付宝 */
public class AlipayUtil {

    public static String connect(AlipayBean alipayBean) throws AlipayApiException {
        //1、获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                AliPayConfig.GATEWAYURL,//支付宝网关
                AliPayConfig.APPID,//appid
                AliPayConfig.MERCHANTPRIVATEKEY,//商户私钥
                "json",
                AliPayConfig.CHARSET,//字符编码格式
                AliPayConfig.ALIPAYPUBLICKEY,//支付宝公钥
                AliPayConfig.SIGNTYPE//签名方式
        );
        //2、设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //页面跳转同步通知页面路径
        alipayRequest.setReturnUrl(AliPayConfig.RETURNURL);
        // 服务器异步通知页面路径
        alipayRequest.setNotifyUrl(AliPayConfig.NOTIFYURL);
        //封装参数
        alipayRequest.setBizContent(JSON.toJSONString(alipayBean));
        //3、请求支付宝进行付款，并获取支付结果
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        //返回付款信息
        return  result;
    }
}
