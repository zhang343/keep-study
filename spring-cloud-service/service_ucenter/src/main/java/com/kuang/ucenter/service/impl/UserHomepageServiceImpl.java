package com.kuang.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.UserHomepage;
import com.kuang.ucenter.entity.vo.HomePageVo;
import com.kuang.ucenter.mapper.UserHomepageMapper;
import com.kuang.ucenter.service.UserHomepageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@Service
@Slf4j
public class UserHomepageServiceImpl extends ServiceImpl<UserHomepageMapper, UserHomepage> implements UserHomepageService {


}
