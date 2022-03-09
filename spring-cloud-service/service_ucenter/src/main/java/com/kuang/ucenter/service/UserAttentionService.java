package com.kuang.ucenter.service;

import com.kuang.ucenter.entity.UserAttention;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.ucenter.entity.vo.UserVo;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface UserAttentionService extends IService<UserAttention> {

    //查询出指定用户的所有粉丝id
    List<String> findUserFansId(String userId);

    //查询用户关注数量
    Future<Integer> findFocusOnNumber(String userId);

    //查询用户关注用户
    Future<List<UserVo>> findFocusOnUser(String userId, Long current, Long limit);

    //查询用户粉丝数量
    Future<Integer> findFansNumber(String userId);

    //查询用户粉丝
    Future<List<UserVo>> findFansUser(String userId, Long current, Long limit);

    //增加用户关注
    void addUserAttention(String myUserId, String userId);

    //查询用户A是否关注了用户B
    boolean findUserAtoUserB(String userIdA , String userIdB);

    //查询用户粉丝数量
    Integer findUserFansNumber(String userId);

    //查询用户关注数量
    Integer findUserAttentionNumber(String userId);
}
