package com.kuang.download.controller;


import com.kuang.download.entity.DtmFile;
import com.kuang.download.entity.vo.DtmFileVo;
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
        List<DtmFileVo> dtmFileList = dtmFileService.findFileCondition(categoryId , fileName);
        return R.ok().data("fileList" , dtmFileList);
    }
}

