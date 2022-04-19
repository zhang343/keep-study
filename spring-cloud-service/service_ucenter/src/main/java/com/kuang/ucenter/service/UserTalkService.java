package com.kuang.ucenter.service;

import com.kuang.ucenter.entity.UserTalk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.ucenter.entity.vo.OtherUserTalkVo;
import com.kuang.ucenter.entity.vo.UserTalkVo;

import java.util.List;


public interface UserTalkService extends IService<UserTalk> {

    //查询用户说说数量
    Integer findUserTalkNumber(String userId);

    //查找用户说说
    List<UserTalkVo> findUserTalk(String userId, Long current, Long limit);

    //用户发表说说
    UserTalkVo publishTalk(String userId, String content);

    //删除用户说说
    void deleteUserTalk(String userId, String id);

    //修改用户说说是否可以公开
    void updateUserTalkIsPublic(String userId, String id, Boolean isPublic);

    //查询他人用户说说数量
    Integer findOtherUserTalkNumber(String userId);

    //查询他人用户说说
    List<OtherUserTalkVo> findOtherUserTalk(String userId, Long current, Long limit);
}
