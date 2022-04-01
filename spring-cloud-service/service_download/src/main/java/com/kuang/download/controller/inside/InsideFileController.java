package com.kuang.download.controller.inside;

import com.kuang.download.entity.DtmFile;
import com.kuang.download.service.DtmFileService;
import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inside/file")
@Slf4j
public class InsideFileController {

    @Resource
    private DtmFileService dtmFileService;

    //通过文件id查找文件名和(这里指阿里云存储)源文件名和价格
    @GetMapping("findFileNameAndPriceById")
    public R findFileNameAndPriceById(String id){
        DtmFile dtmFile = dtmFileService.getById(id);
        return R.ok().data("name" , dtmFile.getName()).
                data("fileSourceId" , dtmFile.getFileSourceId()).
                data("price" , dtmFile.getPrice());
    }
}
