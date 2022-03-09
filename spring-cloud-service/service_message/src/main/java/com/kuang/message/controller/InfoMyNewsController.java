package com.kuang.message.controller;


import com.kuang.message.entity.vo.MyNewsVo;
import com.kuang.message.entity.vo.SystemVo;
import com.kuang.message.service.InfoMyNewsService;
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

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@RestController
@RequestMapping("/message/mynews")
@Slf4j
public class InfoMyNewsController {

    @Resource
    private InfoMyNewsService myNewsService;

    //分页查询我的消息
    @PostMapping("findAll")
    public R findAll(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                     @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                     HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询用户我的消息,用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法查询");
        }
        Integer total = myNewsService.findUserNewsNumber(userId);
        List<MyNewsVo> myNewsVos = myNewsService.findUserNews(current , limit , userId);
        myNewsService.setMyNewsRead(myNewsVos);
        return R.ok().data("total" , total).data("myNewsList" , myNewsVos);
    }
}

