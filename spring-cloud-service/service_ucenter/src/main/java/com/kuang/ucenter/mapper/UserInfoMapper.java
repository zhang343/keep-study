package com.kuang.ucenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.ucenter.entity.UserInfo;
import com.kuang.ucenter.entity.vo.UserSearchVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserInfoMapper extends BaseMapper<UserInfo> {

    //更新用户每日签到
    Integer updateUserIsSign();

    //根据条件查找用户
    List<UserSearchVo> findUserByAccountOrNickname(@Param("accountOrNickname") String accountOrNickname,
                                                   @Param("current") Long current,
                                                   @Param("limit") Long limit);
}
