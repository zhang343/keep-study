package com.kuang.ucenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.UserHomepage;
import com.kuang.ucenter.mapper.UserHomepageMapper;
import com.kuang.ucenter.service.UserHomepageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserHomepageServiceImpl extends ServiceImpl<UserHomepageMapper, UserHomepage> implements UserHomepageService {


    //查询用户简介
    @Override
    public String findUserIntroduce(String userId) {
        UserHomepage userHomepage = baseMapper.selectById(userId);
        return userHomepage.getContent();
    }

    //修改用户主页内容
    @Override
    public void setUserIntroduce(String userId, String content) {
        UserHomepage userHomepage = new UserHomepage();
        userHomepage.setId(userId);
        userHomepage.setContent(content);
        int i = baseMapper.updateById(userHomepage);
        if(i != 1) {
            throw new XiaoXiaException(ResultCode.ERROR , "修改失败");
        }
    }
}
