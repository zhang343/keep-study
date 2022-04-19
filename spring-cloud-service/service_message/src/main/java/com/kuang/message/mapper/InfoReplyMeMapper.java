package com.kuang.message.mapper;

import com.kuang.message.entity.InfoReplyMe;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.message.entity.vo.ReplyMeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface InfoReplyMeMapper extends BaseMapper<InfoReplyMe> {

    //查询回复我的消息
    List<ReplyMeVo> findUserNews(@Param("current") Long current,
                                 @Param("limit") Long limit,
                                 @Param("userId") String userId);

    //设置回复我的消息已读
    void setReplyMeRead(@Param("idList") List<String> idList);
}
