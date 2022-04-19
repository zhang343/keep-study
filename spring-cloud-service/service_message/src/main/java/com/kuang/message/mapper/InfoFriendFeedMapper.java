package com.kuang.message.mapper;

import com.kuang.message.entity.InfoFriendFeed;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.message.entity.vo.FriendFeedVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface InfoFriendFeedMapper extends BaseMapper<InfoFriendFeed> {

    //查询好友动态消息
    List<FriendFeedVo> findUserNews(@Param("current") Long current,
                                    @Param("limit") Long limit,
                                    @Param("userId") String userId);

    //让好友动态消息已读
    Integer setFriendFeedRead(@Param("idList") List<String> idList);
}
