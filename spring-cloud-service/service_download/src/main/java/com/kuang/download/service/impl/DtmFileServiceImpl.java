package com.kuang.download.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.download.entity.DtmFile;
import com.kuang.download.entity.vo.DtmFileVo;
import com.kuang.download.mapper.DtmFileMapper;
import com.kuang.download.service.DtmFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@Service
@Slf4j
public class DtmFileServiceImpl extends ServiceImpl<DtmFileMapper, DtmFile> implements DtmFileService {

    //通过分类id和文件名查找文件
    @Override
    public List<DtmFileVo> findFileCondition(String categoryId, String fileName) {
        log.info("查询文件,categoryId:" + categoryId + ",fileName:" + fileName);
        return baseMapper.findFileCondition(categoryId , fileName);
    }

    //通过文件id查找文件名和(这里指阿里云存储)源文件名和价格
    @Override
    public DtmFile findFileNameAndPriceById(String id) {
        log.info("通过文件id查找文件名和(这里指阿里云存储)源文件名和价格,文件id:" + id);
        DtmFile dtmFile = baseMapper.selectById(id);
        if(dtmFile == null){
            log.warn("文件id:" + id + ",文件未找到");
            throw new XiaoXiaException(ResultCode.ERROR , "没有这个文件");
        }
        return dtmFile;
    }

}
