package com.kuang.ucenter.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class TengXunSmsProperties implements InitializingBean {

    @Value("${tencent.sms.secretid}")
    private String secretId;
    @Value("${tencent.sms.secretkey}")
    private String secretKey;
    @Value("${tencent.sms.appid}")
    private String appid;
    @Value("${tencent.sms.sign}")
    private String sign;
    @Value("${tencent.sms.templateid}")
    private String templateID;

    public static String SECRET_ID;
    public static String SECRET_KEY;
    public static String APP_ID;
    public static String SIGN;
    public static String TEMPLATE_ID;

    @Override
    public void afterPropertiesSet() throws Exception {
        SECRET_ID = secretId;
        SECRET_KEY = secretKey;
        APP_ID = appid;
        SIGN = sign;
        TEMPLATE_ID = templateID;
    }
}
