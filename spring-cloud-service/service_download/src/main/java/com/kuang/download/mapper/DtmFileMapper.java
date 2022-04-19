package com.kuang.download.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.download.entity.DtmFile;
import com.kuang.download.entity.vo.DtmFileVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DtmFileMapper extends BaseMapper<DtmFile> {

    //根据条件查询文件
    List<DtmFileVo> findFileCondition(@Param("categoryId") String categoryId,
                                      @Param("fileName")String fileName);
}
