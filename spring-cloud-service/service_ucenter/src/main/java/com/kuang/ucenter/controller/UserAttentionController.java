package com.kuang.ucenter.controller;


import com.kuang.ucenter.service.UserAttentionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@RestController
@RequestMapping("/user/attention")
@Slf4j
public class UserAttentionController {

    @Resource
    private UserAttentionService userAttentionService;

}

