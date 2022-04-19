package com.kuang.message.mapper;

import com.kuang.message.entity.InfoSystem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.message.entity.vo.SystemVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface InfoSystemMapper extends BaseMapper<InfoSystem> {

    //查看用户消息
    List<SystemVo> findUserNews(@Param("current") Long current,
                                @Param("limit") Long limit,
                                @Param("userId") String userId);

    //让系统消息已读
    Integer setSystemNewsRead(@Param("idList") List<String> idList);
}
