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

    //查找用户数量，通过条件
    Long findUserNumberByCondition(@Param("accountOrNickname") String accountOrNickname);

    //查找用户，通过条件
    List<UserSearchVo> findUserByCondition(@Param("current") Long current,
                                           @Param("limit") Long limit,
                                           @Param("accountOrNickname") String accountOrNickname);


    //查找用户安全信息
    UserSecurity findAWEP(String userId);

    //查询用户基本信息
    UserDetailVo findDetailInformation(String userId);
}
