package com.kuang.download.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.download.entity.DtmFile;
import com.kuang.download.entity.vo.DtmFileVo;

import java.util.List;


public interface DtmFileService extends IService<DtmFile> {

    //通过分类id和文件名查找文件
    List<DtmFileVo> findFileCondition(String categoryId, String fileName);

}
