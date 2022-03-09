package com.kuang.vip.service;

import com.kuang.vip.entity.UserTodayRight;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Xiaozhang
 * @since 2022-02-27
 */
public interface UserTodayRightService extends IService<UserTodayRight> {

    //用户每日文章权益
    void addArticle(String userId);

    //更新用户每日权益
    void updateMemberTodayRight();

    //查看用户是否签到
    Boolean findIsSign(String userId);

    //用户签到
    void toSign(String userId);
}
