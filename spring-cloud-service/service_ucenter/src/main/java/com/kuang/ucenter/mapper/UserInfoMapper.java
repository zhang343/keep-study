package com.kuang.ucenter.mapper;

import com.kuang.ucenter.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.ucenter.entity.vo.UserDetailVo;
import com.kuang.ucenter.entity.vo.UserSearchVo;
import com.kuang.ucenter.entity.vo.UserSecurity;
import com.kuang.ucenter.entity.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    //更新用户每日签到
    Integer updateUserIsSign();
}
