package com.kuang.bbs.controller;


import com.kuang.bbs.service.LabelService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@RestController
@RequestMapping("/bbs/label")
@Slf4j
public class LabelController {

    @Resource
    private LabelService labelService;

    //查询用户标签
    @GetMapping("findUserLabel")
    public R findUserLabel(String userId){
        log.info("查询用户标签:" + userId);
        if(StringUtils.isEmpty(userId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法查询");
        }
        List<String> labelList = labelService.findUserLabel(userId);
        return R.ok().data("labelList" , labelList);
    }

}

