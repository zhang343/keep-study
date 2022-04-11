package com.kuang.ucenter.service.impl;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.RedisUtils;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.service.MsmService;
import com.kuang.ucenter.utils.TengXunSmsProperties;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MsmServiceImpl implements MsmService {

    //发送短信
    @Override
    public void sendMsm(String phone , String code) {

        Object value = RedisUtils.getValue(phone);
        if(!StringUtils.isEmpty(value)){
            return;
        }


        //发送参数
        String secretId = TengXunSmsProperties.SECRET_ID;
        String secretKey = TengXunSmsProperties.SECRET_KEY;
        String appid = TengXunSmsProperties.APP_ID;
        String sign = TengXunSmsProperties.SIGN;
        System.out.println(sign);
        String templateID = TengXunSmsProperties.TEMPLATE_ID;

        //设置发送号码和消息内容
        String jointMobile = "+86" + phone;
        String[] phoneNumbers = {jointMobile};
        String[] templateParams = { code , "5"};//对应模板中{1} 和 {2}

        //实例化一个认证对象，入参需要传入腾讯云账户密钥对 secretId 和 secretKey
        Credential cred = new Credential(secretId , secretKey);
        //设置发送信息参数
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod("HmacSHA256");
        //设置客户端对象
        SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);

        //设置请求对象
        SendSmsRequest req = new SendSmsRequest();
        req.setSmsSdkAppid(appid);
        req.setSign(sign);
        req.setTemplateID(templateID);
        req.setPhoneNumberSet(phoneNumbers);
        req.setTemplateParamSet(templateParams);


        //响应对象
        SendSmsResponse res = null;


        try {
            //发送短信
            res = client.SendSms(req);
        } catch(TencentCloudSDKException e) {
            throw new XiaoXiaException(ResultCode.ERROR, "失败");
        }

        RedisUtils.setValueTimeout(phone , code , 300);
    }
}
