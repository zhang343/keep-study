package com.kuang.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.message.entity.InfoMyNews;
import com.kuang.message.entity.vo.MyNewsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface InfoMyNewsMapper extends BaseMapper<InfoMyNews> {

    //查询用户我的消息
    List<MyNewsVo> findUserNews(@Param("current") Long current,
                                @Param("limit") Long limit,
                                @Param("userId") String userId);

    //让用户我的消息已读
    Integer setMyNewsRead(@Param("idList") List<String> idList);
}
