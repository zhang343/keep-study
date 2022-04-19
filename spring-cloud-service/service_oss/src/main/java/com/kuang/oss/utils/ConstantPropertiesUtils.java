package com.kuang.oss.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ConstantPropertiesUtils implements InitializingBean {

    //读取配置文件
    @Value("${aliyun.oss.file.keyid}")
    private String keyId;
    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;
    @Value("${aliyun.oss.file.publicendpoint}")
    private String publicendpoint;
    @Value("${aliyun.oss.file.publicbucketname}")
    private String publicbucketName;
    @Value("${aliyun.oss.file.privateendpoint}")
    private String privateendpoint;
    @Value("${aliyun.oss.file.privatebucketname}")
    private String privatebucketName;




    //密钥
    public static String PUBLIC_END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String PUBLIC_BUCKET_NAME;
    public static String PRIVATE_END_POINT;
    public static String PRIVATE_BUCKET_NAME;


    //对象创建之后执行的方法
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("取出阿里云密匙和相关数据，进行存储");
        ConstantPropertiesUtils.PUBLIC_END_POINT = publicendpoint;
        ConstantPropertiesUtils.ACCESS_KEY_ID = keyId;
        ConstantPropertiesUtils.ACCESS_KEY_SECRET = keySecret;
        ConstantPropertiesUtils.PUBLIC_BUCKET_NAME = publicbucketName;
        ConstantPropertiesUtils.PRIVATE_END_POINT = privateendpoint;
        ConstantPropertiesUtils.PRIVATE_BUCKET_NAME = privatebucketName;
    }


}
