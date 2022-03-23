package com.kuang.ucenter.controller;

import com.kuang.ucenter.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user/sign")
@Slf4j
public class UserSignController {

    @Resource
    private UserInfoService userInfoService;
}