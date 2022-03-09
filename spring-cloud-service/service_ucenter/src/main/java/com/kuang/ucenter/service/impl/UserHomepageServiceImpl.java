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
        wrapper.select("id" , "version");
        wrapper.eq("id" , userId);
        UserHomepage userHomepage = baseMapper.selectOne(wrapper);
        if(userHomepage == null){
            //说明没有
            userHomepage = new UserHomepage();
            userHomepage.setId(userId);
            userHomepage.setContent(content);
            int insert = baseMapper.insert(userHomepage);
            if(insert != 1){
                throw new XiaoXiaException(ResultCode.ERROR , "修改用户主页内容失败");
            }
            return;
        }
        // 到了这里说明有
        userHomepage.setContent(content);
        int i = baseMapper.updateById(userHomepage);
        if(i != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "修改用户主页内容失败");
        }
    }

}
