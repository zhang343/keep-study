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


@Service
@Slf4j
public class DtmFileServiceImpl extends ServiceImpl<DtmFileMapper, DtmFile> implements DtmFileService {

    //通过分类id和文件名查找文件
    @Override
    public List<DtmFileVo> findFileCondition(String categoryId, String fileName) {
        return baseMapper.findFileCondition(categoryId , fileName);
    }

}
