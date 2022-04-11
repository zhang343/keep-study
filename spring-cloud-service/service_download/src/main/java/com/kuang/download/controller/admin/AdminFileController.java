package com.kuang.download.controller.admin;

import com.kuang.download.entity.DtmFile;
import com.kuang.download.entity.vo.uploadFileVo;
import com.kuang.download.service.DtmFileService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dtm/admin")
@Slf4j
public class AdminFileController {

    @Resource
    private DtmFileService fileService;

    //增加文件
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("addFile")
    public R addFile(uploadFileVo uploadFileVo){
        if(StringUtils.isEmpty(uploadFileVo.getCategoryId()) ||
                StringUtils.isEmpty(uploadFileVo.getName()) ||
                StringUtils.isEmpty(uploadFileVo.getSize()) ||
                StringUtils.isEmpty(uploadFileVo.getFileSourceId()) ||
                uploadFileVo.getPrice() < 0){
            throw new XiaoXiaException(ResultCode.ERROR , "请传入正确的参数");
        }

        DtmFile dtmFile = new DtmFile();
        BeanUtils.copyProperties(uploadFileVo , dtmFile);
        boolean save = fileService.save(dtmFile);
        if(!save){
            throw new XiaoXiaException(ResultCode.ERROR , "保存文件失败");
        }

        return R.ok().data("fileId" , dtmFile.getId());
    }


    //删除文件
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("deleteFile")
    public R deleteFile(String fileId){
        boolean b = fileService.removeById(fileId);
        if(!b){
            throw new XiaoXiaException(ResultCode.ERROR , "删除文件失败");
        }
        return R.ok();
    }

}
