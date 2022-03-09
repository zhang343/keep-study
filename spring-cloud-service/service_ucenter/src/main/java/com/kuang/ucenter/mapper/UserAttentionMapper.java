package com.kuang.ucenter.mapper;

import com.kuang.ucenter.entity.UserAttention;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.ucenter.entity.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface UserAttentionMapper extends BaseMapper<UserAttention> {

    //查询用户关注
    List<UserVo> findFocusOnUser(@Param("userId") String userId,
                                 @Param("current") Long current,
                                 @Param("limit") Long limit);

    //查询用户粉丝
    List<UserVo> findFansUser(@Param("userId") String userId,
                              @Param("current") Long current,
                              @Param("limit") Long limit);
}
