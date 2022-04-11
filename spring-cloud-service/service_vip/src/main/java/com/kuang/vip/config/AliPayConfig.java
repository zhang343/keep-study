package com.kuang.vip.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "alipay")
@Data
public class AliPayConfig implements InitializingBean {

    private String appid;
    private String merchantPrivateKey;
    private String alipayPublicKey;
    private String notifyUrl;
    private String returnUrl;
    private String signType;
    private String charset;
    private String gatewayUrl;

    public static String APPID;
    public static String MERCHANTPRIVATEKEY;
    public static String ALIPAYPUBLICKEY;
    public static String NOTIFYURL;
    public static String RETURNURL;
    public static String SIGNTYPE;
    public static String CHARSET;
    public static String GATEWAYURL;

    @Override
    public void afterPropertiesSet() throws Exception {
        APPID = appid;
        MERCHANTPRIVATEKEY = merchantPrivateKey;
        ALIPAYPUBLICKEY = alipayPublicKey;
        NOTIFYURL = notifyUrl;
        RETURNURL = returnUrl;
        SIGNTYPE = signType;
        CHARSET = charset;
        GATEWAYURL = gatewayUrl;
    }
}


