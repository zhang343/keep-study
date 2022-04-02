package com.kuang.ucenter.controller;


import com.google.gson.Gson;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.rabbitmq.MsgProducer;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.UserInfo;
import com.kuang.ucenter.service.UserInfoService;
import com.kuang.ucenter.utils.ConstantWxUtils;
import com.kuang.ucenter.utils.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;


@RestController
@RequestMapping("/user/wx")
@Slf4j
public class WxApiController {

    @Resource
    private UserInfoService userInfoService;

    //生成微信登录二维码
    @GetMapping("login")
    public R login() {
        //微信开放平台授权baseUrl  %s相当于?代表占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //对redirect_url进行URLEncoder编码,redirect_url是在用户扫描登录后，浏览器会自动调用的一个接口
        String redirectUrl = null;
        try {
            redirectUrl = URLEncoder.encode(ConstantWxUtils.WX_OPEN_REDIRECT_URL, String.valueOf(StandardCharsets.UTF_8));
        } catch(Exception e) {
            throw new XiaoXiaException(ResultCode.ERROR , "微信二维码生成失败");
        }
        //置换baseUrl里面的%s
        String url = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu"
        );

        return R.ok().data("url" , url);
    }


    //用户扫描微信回调方法
    @GetMapping("callback")
    public R callback(String code, String state) {
        //用code向微信请求响应值，accsess_token 和 openid
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        //拼接三个参数 ：id、秘钥、code
        String accessTokenUrl = String.format(
                baseAccessTokenUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                ConstantWxUtils.WX_OPEN_APP_SECRET,
                code
        );

        //远程调用微信接口
        String accessTokenInfo = null;
        try {
            accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
        } catch(Exception e) {
            throw new XiaoXiaException(ResultCode.ERROR,"微信远程调用失败");
        }

        //取得响应值
        Gson gson = new Gson();
        HashMap mapAccessToken = gson.fromJson(accessTokenInfo , HashMap.class);
        String access_token = (String) mapAccessToken.get("access_token");
        String openid = (String) mapAccessToken.get("openid");

        //查询用户
        UserInfo member = userInfoService.getOpenIdMember(openid);
        if(member == null){
            //用户不存在，远程调用微信接口，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            //拼接两个参数
            String userInfoUrl = String.format(
                    baseUserInfoUrl,
                    access_token,
                    openid
            );
            //发送请求，获取用户信息
            String userInfo = null;
            try {
                userInfo = HttpClientUtils.get(userInfoUrl);
            } catch(Exception e) {
                throw new XiaoXiaException(ResultCode.ERROR,"微信远程调用失败");
            }
            //获取返回userinfo字符串扫描人信息
            HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
            String nickname = (String)userInfoMap.get("nickname");//昵称
            String headimgurl = (String)userInfoMap.get("headimgurl");//头像

            member = userInfoService.insertMember(openid , nickname , headimgurl);

            userInfoService.setMyRegisterNews(member.getId());
        }
        String id = member.getId();
        String jwtToken = JwtUtils.getJwtToken(id);
        return R.ok().data("token" , jwtToken);
    }

}
