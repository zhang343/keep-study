package com.kuang.download.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.download.entity.DtmFile;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface DtmFileService extends IService<DtmFile> {

    //通过分类id和文件名查找文件
    List<DtmFile> findFileCondition(String categoryId, String fileName);
    //通过文件id查找文件名和(这里指阿里云存储)源文件名和价格
    DtmFile findFileNameAndPriceById(String id);
}
