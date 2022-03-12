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

    //修改用户主页内容
    @Override
    public void updateContent(String content , String userId) {
        log.info("修改用户主页内容");
        QueryWrapper<UserHomepage> wrapper = new QueryWrapper<>();
        wrapper.eq("id" , userId);
        Integer integer = baseMapper.selectCount(wrapper);
        UserHomepage userHomepage = new UserHomepage();
        userHomepage.setId(userId);
        userHomepage.setContent(content);
        if(integer != 1){
            //说明没有
            int insert = baseMapper.insert(userHomepage);
            if(insert != 1){
                throw new XiaoXiaException(ResultCode.ERROR , "修改用户主页内容失败");
            }
        }else {
            int i = baseMapper.updateById(userHomepage);
            if(i != 1){
                throw new XiaoXiaException(ResultCode.ERROR , "修改用户主页内容失败");
            }
        }
    }

    //查看主页内容
    @Override
    public UserHomepage findByUserId(String userId) {
        UserHomepage userHomepage = baseMapper.selectById(userId);
        if(userHomepage == null){
            userHomepage = new UserHomepage();
        }
        return userHomepage;
    }

}
