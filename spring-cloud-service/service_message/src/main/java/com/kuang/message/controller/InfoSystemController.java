package com.kuang.message.controller;


import com.kuang.message.entity.vo.SystemVo;
import com.kuang.message.service.InfoSystemService;
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
@RequestMapping("/message/system")
@Slf4j
public class InfoSystemController {

    @Resource
    private InfoSystemService systemService;

    //分页查询系统消息
    @PostMapping("findAll")
    public R findAll(@RequestParam(value = "current", required = false, defaultValue = "1") Long current ,
                     @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit ,
                     HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("查询用户系统消息,用户id:" + userId);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法查询");
        }
        Integer total = systemService.findUserNewsNumber(userId);
        List<SystemVo> systemVos = systemService.findUserNews(current , limit , userId);
        systemService.setSystemNewsRead(systemVos);
        return R.ok().data("total" , total).data("systemNewsList" , systemVos);
    }

}

