package com.kuang.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.ucenter.entity.UserColumn;
import com.kuang.ucenter.mapper.UserColumnMapper;
import com.kuang.ucenter.service.UserColumnService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@Service
@Slf4j
public class UserColumnServiceImpl extends ServiceImpl<UserColumnMapper, UserColumn> implements UserColumnService {

    //查询用户专栏数量
    @Override
    public Integer findColumnNumberByUserId(String userId) {
        log.info("查询用户专栏数量");
        QueryWrapper<UserColumn> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }
}
