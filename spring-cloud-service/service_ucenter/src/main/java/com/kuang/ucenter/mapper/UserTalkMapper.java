package com.kuang.ucenter.mapper;

import com.kuang.ucenter.entity.UserTalk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.ucenter.entity.vo.UserTalkVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface UserTalkMapper extends BaseMapper<UserTalk> {

    //查找用户说说
    List<UserTalkVo> findUserTalk(@Param("userId") String userId,
                                  @Param("current") Long current,
                                  @Param("limit") Long limit);
}
