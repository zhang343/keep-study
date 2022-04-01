package com.kuang.message.controller;


import com.kuang.message.entity.vo.FriendFeedVo;
import com.kuang.message.service.InfoFriendFeedService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
        //数据校验
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请先登录");
        }
        List<FriendFeedVo> friendFeedVos = friendFeedService.findUserNews(current, limit, userId);
        Integer total = friendFeedService.findUserNewsNumber(userId);
        friendFeedService.setFriendFeedRead(friendFeedVos);
        return R.ok().data("total" , total).data("dynamicNewList" , friendFeedVos);
    }

}

