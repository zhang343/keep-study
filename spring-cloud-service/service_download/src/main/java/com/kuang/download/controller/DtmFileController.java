package com.kuang.download.controller;


import com.kuang.download.entity.DtmFile;
import com.kuang.download.service.DtmFileService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@RestController
@RequestMapping("/dtm/file")
@Slf4j
public class DtmFileController {

    @Resource
    private DtmFileService dtmFileService;

    //通过分类id和文件名查找文件
    @GetMapping("findFileCondition")
    public R findFileCondition(String categoryId , String fileName){
        log.info("开始查找文件,categoryId:" + categoryId + ",fileName:" + fileName);
        if(StringUtils.isEmpty(categoryId) && StringUtils.isEmpty(fileName)){
            throw new XiaoXiaException(ResultCode.ERROR , "请传入参数");
        }
        List<DtmFile> dtmFileList = dtmFileService.findFileCondition(categoryId , fileName);
        return R.ok().data("fileList" , dtmFileList);
    }

    //通过文件id查找文件名和(这里指阿里云存储)源文件名和价格
    @GetMapping("findFileNameAndPriceById")
    public R findFileNameAndPriceById(String id){
        log.info("通过文件id查找文件名和(这里指阿里云存储)源文件名和价格,文件id:" + id);
        if(StringUtils.isEmpty(id)){
            log.warn("有人可能涉及非法操作");
            throw new XiaoXiaException(ResultCode.ERROR , "你没有传入文件id");
        }
        DtmFile dtmFile = dtmFileService.findFileNameAndPriceById(id);
        return R.ok().data("name" , dtmFile.getName()).data("fileSourceId" , dtmFile.getFileSourceId()).data("price" , dtmFile.getPrice());
    }
}

