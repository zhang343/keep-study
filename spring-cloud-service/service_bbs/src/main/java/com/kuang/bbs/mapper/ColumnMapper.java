package com.kuang.bbs.mapper;

import com.kuang.bbs.entity.Column;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.bbs.entity.vo.ColumnArticleVo;
import com.kuang.bbs.entity.vo.ColumnVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ColumnMapper extends BaseMapper<Column> {

    //查询用户专栏
    List<ColumnVo> findUserColumn(String userId);

    //查询他人专栏
    List<ColumnVo> findOtherUserColumn(String userId);

    //更新专栏浏览量
    void updateColumnViews(@Param("columnUpdateList") List<Column> columnUpdateList);
}
