package com.kuang.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.kuang.oss.service.OssService;
import com.kuang.oss.utils.ConstantPropertiesUtils;
import com.kuang.oss.utils.FileUtils;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.springcloud.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author XiaoZhang
 * @date 2022/2/3 15:21
 * 图片处理实现接口
 */
@Service
@Slf4j
public class OssServiceImpl implements OssService {

    //上传图片接口
    @Override
    public String uploadPicture(MultipartFile picture) {
        log.info("上传图片前相关阿里云数据准备");
        String endpoint = ConstantPropertiesUtils.PUBLIC_END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.PUBLIC_BUCKET_NAME;
        String objectName = upload(picture, endpoint, accessKeyId, accessKeySecret, bucketName);
        String url = "https://" + bucketName + "." + endpoint + "/" + objectName;
        log.info("图片上传成功,浏览器访问地址为:" + url);
        return url;
    }

    //上传图片、文件方法
    private String upload(MultipartFile file ,
                          String endpoint ,
                          String accessKeyId ,
                          String accessKeySecret ,
                          String bucketName){
        log.info("开始进行文件上传到阿里云");
        //创建上传文件到阿里云Oss对象
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String dir = new DateTime().toString("yyyy/MM/dd");
        //设置文件在阿里云的存储名
        String originalFilename = file.getOriginalFilename();
        String suffix = FileUtils.getFileSuffix(originalFilename);
        String fileName = UUIDUtil.getUUID() + "." + suffix;
        String objectName = dir + "/" + fileName;
        log.info("上传文件存储在Oss里面的地址:" + objectName);
        InputStream inputStream = null;
        try {
            log.info("尝试获取文件数据流:" + fileName);
            inputStream = file.getInputStream();
        } catch(IOException e) {
            log.error("获取文件数据流失败:" + fileName);
            throw new XiaoXiaException(ResultCode.ERROR, "上传文件失败");
        }
        //上传文件
        log.info("上传文件并关闭Oss对象");
        ossClient.putObject(bucketName, objectName, inputStream);
        //关闭Oss对象
        ossClient.shutdown();
        return objectName;
    }

    //上传文件接口
    @Override
    public String uploadFile(MultipartFile file) {
        //设置上传参数
        log.info("上传图片前相关阿里云数据准备");
        String endpoint = ConstantPropertiesUtils.PRIVATE_END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.PRIVATE_BUCKET_NAME;
        String objectName = upload(file, endpoint, accessKeyId, accessKeySecret, bucketName);
        log.info("上传文件到阿里云成功,文件存储地址:" + objectName);
        return objectName;
    }

    //下载文件
    @Override
    public void downloadFile(String fileSourceId , String name , HttpServletResponse response) {
        log.info("开始下载文件,文件名(阿里云):"  + fileSourceId);
        String endpoint = ConstantPropertiesUtils.PRIVATE_END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.PRIVATE_BUCKET_NAME;
        //创建对象Oss
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //创建下载对象
        OSSObject ossObject = ossClient.getObject(bucketName, fileSourceId);
        //取得下载流
        InputStream inputStream = ossObject.getObjectContent();
        String suffix = FileUtils.getFileSuffix(fileSourceId);
        name = name + "." + suffix;
        try {
            log.info("对文件名进行重编码,utf-8 --> ISO8859-1");
            name = new String(name.getBytes(StandardCharsets.UTF_8) , "ISO8859-1");
        } catch(UnsupportedEncodingException e) {
            log.warn("对文件名进行重编码,utf-8 --> ISO8859-1失败,使用uuid生成名字");
            name = UUIDUtil.getUUID() + "." + suffix;
        }
        response.setHeader("content-disposition" , "attachment;filename=" + name);

        OutputStream outputStream = null;
        try {
            log.info("获取response输出流OutputStream");
            outputStream = response.getOutputStream();
        } catch(IOException e) {
            log.error("获取response输出流OutputStream失败");
            throw new XiaoXiaException(ResultCode.ERROR , "下载文件失败");
        }

        byte[] bytes = new byte[1024];
        int len = -1;
        try {
            log.info("进行文件数据读出和写入");
            while((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes , 0 , len);
            }
        }catch(IOException e){
            log.error("进行文件数据读出和写入出现错误");
            throw new XiaoXiaException(ResultCode.ERROR , "下载文件失败");
        }


        try {
            inputStream.close();
        } catch(IOException e1) {
            try {
                inputStream.close();
            }catch(IOException e2){
                log.warn("文件读入流关闭失败");
            }
        }
        log.info("关闭Oss对象");
        ossClient.shutdown();
    }

}
