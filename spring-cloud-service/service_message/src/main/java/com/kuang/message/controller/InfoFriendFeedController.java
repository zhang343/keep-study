package com.kuang.message.controller;


import com.kuang.message.entity.vo.FriendFeedVo;
import com.kuang.message.service.InfoFriendFeedService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@RestController
@RequestMapping("/message/friendsFeed")
@Slf4j
public class InfoFriendFeedController {

    @Resource
    private InfoFriendFeedService friendFeedService;

    //查询好友动态消息
    @PostMapping("findAll")
    public R findAll(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                     @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                     HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询好友动态消息,用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法查询");
        }
        List<FriendFeedVo> friendFeedVos = friendFeedService.findUserNews(current, limit, userId);
        Future<List<FriendFeedVo>> articleViewsAndVipLevel = friendFeedService.findArticleViewsAndVipLevel(friendFeedVos);
        Integer total = friendFeedService.findUserNewsNumber(userId);
        friendFeedService.setFriendFeedRead(friendFeedVos);
        for(int i = 1 ; i < 10 ; i++){
            if(articleViewsAndVipLevel.isDone()){
                break;
            }
            //如果没有执行完毕，则最多等待0.2秒
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch(InterruptedException e) {
                log.warn("休眠失败");
            }
        }
        return R.ok().data("total" , total).data("dynamicNewList" , friendFeedVos);
    }

}

