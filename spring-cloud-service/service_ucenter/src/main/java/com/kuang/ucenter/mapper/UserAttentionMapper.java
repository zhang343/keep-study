package com.kuang.ucenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.ucenter.entity.UserAttention;
import com.kuang.ucenter.entity.vo.UserFollowOrFans;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserAttentionMapper extends BaseMapper<UserAttention> {

    //查询关注用户
    List<UserFollowOrFans> findUserFollow(@Param("userId") String userId,
                                          @Param("current") Long current,
                                          @Param("limit") Long limit);

    //查询粉丝
    List<UserFollowOrFans> findUserFans(@Param("userId") String userId,
                                        @Param("current") Long current,
                                        @Param("limit") Long limit);
}
