package com.kuang.ucenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.ucenter.entity.UserAttention;
import com.kuang.ucenter.entity.vo.UserFollowOrFans;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface UserAttentionService extends IService<UserAttention> {

    //查询出指定用户的所有粉丝id
    List<String> findUserFansId(String userId);


    //查询用户关注数量
    Integer findUserFollowNumber(String userId);

    //查询用户粉丝数量
    Integer findUserFansNumber(String userId);

    //查询关注用户
    List<UserFollowOrFans> findUserFollow(String userId, Long current, Long limit);

    //查询粉丝
    List<UserFollowOrFans> findUserFans(String userId, Long current, Long limit);

    //查询A是否关注了B
    boolean findAIsAttentionB(String myuserId, String userId);

    //增加用户关注
    void addUserAttention(String userId, String otherUserId);

    //取消用户关注
    void deleteUserAttention(String userId, String otherUserId);
}
