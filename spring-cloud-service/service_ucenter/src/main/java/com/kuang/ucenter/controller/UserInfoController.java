package com.kuang.ucenter.controller;


import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.client.BbsClient;
import com.kuang.ucenter.entity.UserInfo;
import com.kuang.ucenter.entity.vo.HomePageVo;
import com.kuang.ucenter.entity.vo.MyUserInfoVo;
import com.kuang.ucenter.entity.vo.UserDetailVo;
import com.kuang.ucenter.entity.vo.UserSetDataVo;
import com.kuang.ucenter.service.UserAttentionService;
import com.kuang.ucenter.service.UserInfoService;
import com.kuang.ucenter.service.UserStudyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@RestController
@RequestMapping("/user/account")
@Slf4j
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserAttentionService userAttentionService;

    @Resource
    private BbsClient bbsClient;

    @Resource
    private UserStudyService userStudyService;

    //查询系统用户数量
    @GetMapping("findUserNumber")
    public R findUserNumber(){
        log.info("查询系统用户数量");
        int userNumber = userInfoService.findUserNumber();
        return R.ok().data("userNumber" , userNumber);
    }

    //查询用户头像和昵称
    @GetMapping("findAvatarAndNicknameByUserId")
    public R findAvatarAndNicknameByUserId(HttpServletRequest request){
        log.info("开始查询用户头像和昵称");
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            log.warn("非法查询用户头像和昵称");
            throw new XiaoXiaException(ResultCode.ERROR , "查询用户头像和昵称失败");
        }
        UserInfo userInfo = userInfoService.findAvatarAndNicknameByUserId(userId);
        return R.ok().data("avatar" , userInfo.getAvatar()).data("nickname" , userInfo.getNickname());
    }


    //用户账号密码登录
    @PostMapping("login")
    public R login(String loginAct , String loginPwd){
        log.info("用户登录,账号:" + loginAct + ",密码:" + loginPwd);
        if(StringUtils.isEmpty(loginAct) || StringUtils.isEmpty(loginPwd)){
            throw new XiaoXiaException(ResultCode.ERROR , "登录失败");
        }
        String userId = userInfoService.login(loginAct , loginPwd);
        String token = JwtUtils.getJwtToken(userId);
        return R.ok().data("token" , token);
    }

    //查询我的账号信息（这里指登录之后小页面的内容）
    @GetMapping("mydetailsmallpage")
    public R mydetailsmallpage(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        String token = request.getHeader("token");
        log.info("查询我的账号信息（这里指登录之后小页面的内容）,用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }

        Future<MyUserInfoVo> userRIV = userInfoService.findUserRIV(token);

        Integer userAttentionNumber = userAttentionService.findUserAttentionNumber(userId);
        Integer userFansNumber = userAttentionService.findUserFansNumber(userId);
        UserInfo byId = userInfoService.getById(userId);
        if(byId == null){
            throw new XiaoXiaException(ResultCode.ERROR ,  "请不要非法操作");
        }
        MyUserInfoVo myUserInfoVo = new MyUserInfoVo();
        BeanUtils.copyProperties(byId , myUserInfoVo);
        myUserInfoVo.setAttentionNumber(userAttentionNumber);
        myUserInfoVo.setFansNumber(userFansNumber);
        MyUserInfoVo myUserInfoVo1 = null;
        try {
            //最多等待0.2秒
            myUserInfoVo1 = userRIV.get(200 , TimeUnit.MILLISECONDS);
            myUserInfoVo.setIsSignIn(myUserInfoVo1.getIsSignIn());
            myUserInfoVo.setVipLevel(myUserInfoVo1.getVipLevel());
            myUserInfoVo.setReleaseArticleNumber(myUserInfoVo1.getReleaseArticleNumber());
        }catch(Exception e){
            log.warn("查询发布文章数量，vip等级、是否签到失败");
            throw new XiaoXiaException(ResultCode.ERROR , "查询失败");
        }
        return R.ok().data("userInfo" , myUserInfoVo);
    }




    //查询我的账号信息（这里指进入我的主页之后右边框的内容）
    @GetMapping("mydetailrightmargin")
    public R mydetailrightmargin(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        String token = request.getHeader("token");
        log.info("查询我的账号信息（这里指进入我的主页之后右边框的内容）,用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法查询");
        }
        //远程调用查询用户vip、已经发布但没有违规的文章数量、评论数，异步
        Future<HomePageVo> vacNumber = userInfoService.findVACNumber(userId , token);
        //为了执行足够的时间让上面的能够执行完毕，下面都不异步
        Integer studyNumberByUserId = userStudyService.findStudyNumberByUserId(userId);
        UserInfo byId = userInfoService.getById(userId);
        HomePageVo homePageVo = new HomePageVo();
        BeanUtils.copyProperties(byId , homePageVo);
        homePageVo.setStudyNumber(studyNumberByUserId);
        try {
            //等待执行完成
            HomePageVo homePageVo1 = vacNumber.get();
            homePageVo.setVipLevel(homePageVo1.getVipLevel());
            homePageVo.setArticleRealeaseNumber(homePageVo1.getArticleRealeaseNumber());
            homePageVo.setCommentNumber(homePageVo1.getCommentNumber());
        }catch(Exception e){
            log.warn("查询用户信息失败");
            throw new XiaoXiaException(ResultCode.ERROR , "查询用户信息失败");
        }

        return R.ok().data("homePage" , homePageVo);
    }


    //查询我的账号信息（这里指进入我的主页之后上边框的内容）
    @GetMapping("mydetailonborder")
    public R mydetailonborder(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询我的账号信息（这里指进入我的主页之后上边框的内容）,用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法查询");
        }
        UserDetailVo userDetailVo = new UserDetailVo();
        //我们最先查询出用户专栏、学习、说说、关注、粉丝数量，此需查5个表，速度较慢，异步调用
        Future<UserDetailVo> csdafNumber = userInfoService.findCSDAFNumber(userDetailVo, userId);
        //查询用户基本信息，为了执行足够的时间让上面的能够执行完毕，下面都不异步
        userInfoService.findDetailInformation(userDetailVo , userId);
        //查询用户所有文章数量
        R BbsR = bbsClient.findUserArticleNumber();
        userDetailVo.setArticleNumber((Integer) BbsR.getData().get("articleNumber"));
        try {
            //等待结果
            csdafNumber.get();
        }catch(Exception e){
            log.warn("查询出用户专栏、学习、说说、关注、粉丝数量失败");
        }
        return R.ok().data("userDetail" , userDetailVo);
    }


    //用户设置修改资料里面的查询
    @GetMapping("setdataquery")
    public R setdataquery(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("用户设置修改资料里面的查询,用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法查询");
        }
        UserSetDataVo userSetDataVo = userInfoService.setdataquery(userId);
        return R.ok().data("userInfo" , userSetDataVo);
    }


}

