package com.kuang.vip.mapper;

import com.kuang.vip.entity.UserTodayRight;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author Xiaozhang
 * @since 2022-02-27
 */
public interface UserTodayRightMapper extends BaseMapper<UserTodayRight> {

    //更新用户每日权益
    Integer updateMemberTodayRight();
}
