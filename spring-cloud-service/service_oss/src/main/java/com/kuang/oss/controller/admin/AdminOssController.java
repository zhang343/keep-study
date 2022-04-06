package com.kuang.oss.controller.admin;

import com.kuang.oss.service.OssService;
import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.concurrent.Future;

@Controller
@RequestMapping("/oss/admin")
@Slf4j
public class AdminOssController {

    @Resource
    private OssService ossService;

    //上传文件
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("uploadFile")
    public R uploadFile(MultipartFile File){
        String fileSourceId = ossService.uploadFile(File);
        Future<String> fileLength = ossService.getFileLength(File);
        String size = "";
        try {
            size = fileLength.get();
        }catch(Exception e){
            throw new RuntimeException();
        }
        return R.ok().data("fileSourceId" , fileSourceId).data("size" , size);
    }
}
