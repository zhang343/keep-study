package com.kuang.ucenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.ucenter.entity.UserInfo;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    //更新用户每日签到
    Integer updateUserIsSign();
}
