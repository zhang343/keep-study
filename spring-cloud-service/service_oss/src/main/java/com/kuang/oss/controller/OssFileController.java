package com.kuang.oss.controller;

import com.kuang.oss.client.DownloadClient;
import com.kuang.oss.client.UcenterClient;
import com.kuang.oss.service.OssService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author XiaoZhang
 * @date 2022/2/3 14:32
 * 文件处理类
 */
@Controller
@RequestMapping("/oss/file")
@Slf4j
public class OssFileController {

    @Resource
    private OssService ossService;

    @Resource
    private DownloadClient downloadClient;

    @Resource
    private UcenterClient ucenterClient;

    //下载文件接口
    @PostMapping("downloadFile")
    public void downloadFile(String id , HttpServletRequest request , HttpServletResponse response){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        log.info("有用户下载文件,文件的id:" + id + ",用户id:" + userId);
        if(StringUtils.isEmpty(id) || userId == null){
            log.warn("有用户非法下载文件,文件的id:" + id + ",用户id:" + userId);
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        R downloadR = downloadClient.findFileNameAndPriceById(id);
        if(!downloadR.getSuccess()){
            log.error("远程调用service-download下面的服务接口/dtm/file/findFileNameAndPriceById失败,文件id:" + id);
            throw new XiaoXiaException(ResultCode.ERROR , "文件未找到");
        }
        Integer price = (Integer) downloadR.getData().get("price");
        if(price != 0){
            log.info("开始远程调用service-ucenter下面的服务接口/KCoin/reduce,减去用户k币:" + price);
            R ucenterR = ucenterClient.reduce(price);
            if(!ucenterR.getSuccess()){
                log.error("远程调用service-ucenter下面的服务接口/KCoin/reduce失败,减去用户k币失败");
                throw new XiaoXiaException(ResultCode.ERROR , "k币不足");
            }
        }
        String fileSourceId = (String) downloadR.getData().get("fileSourceId");
        String name = (String) downloadR.getData().get("name");
        log.info("开始下载文件,文件名(阿里云):" + fileSourceId + ",文件名:" + name);
        ossService.downloadFile(fileSourceId , name , response);
    }

}
