package com.kuang.download.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.download.entity.DtmFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface DtmFileMapper extends BaseMapper<DtmFile> {

    //根据条件查询文件
    List<DtmFile> findFileCondition(@Param("categoryId") String categoryId, @Param("fileName")String fileName);
}
