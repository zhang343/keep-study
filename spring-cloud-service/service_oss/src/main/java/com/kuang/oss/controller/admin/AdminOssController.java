package com.kuang.oss.controller.admin;

import com.kuang.oss.service.OssService;
import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/oss/admin")
@Slf4j
public class AdminOssController {

    @Resource
    private OssService ossService;

    //上传文件,并计算文件大小
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("uploadFile")
    public R uploadFile(MultipartFile file){
        String fileSourceId = ossService.uploadFile(file);
        String fileLength = ossService.getFileLength(file);
        return R.ok().data("fileSourceId" , fileSourceId).data("fileLength" , fileLength);
    }

}
