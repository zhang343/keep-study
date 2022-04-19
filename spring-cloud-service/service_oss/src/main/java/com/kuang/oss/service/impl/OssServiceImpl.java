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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;


@Service
@Slf4j
public class OssServiceImpl implements OssService {

    //上传图片接口
    @Override
    public String uploadPicture(MultipartFile picture) {
        String endpoint = ConstantPropertiesUtils.PUBLIC_END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.PUBLIC_BUCKET_NAME;
        String objectName = upload(picture, endpoint, accessKeyId, accessKeySecret, bucketName);
        String url = "https://" + bucketName + "." + endpoint + "/" + objectName;
        return url;
    }

    //上传图片、文件方法
    private String upload(MultipartFile file ,
                          String endpoint ,
                          String accessKeyId ,
                          String accessKeySecret ,
                          String bucketName){
        //创建上传文件到阿里云Oss对象
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String dir = new DateTime().toString("yyyy/MM/dd");
        //设置文件在阿里云的存储名
        String originalFilename = file.getOriginalFilename();
        String suffix = FileUtils.getFileSuffix(originalFilename);
        String fileName = UUIDUtil.getUUID() + "." + suffix;
        String objectName = dir + "/" + fileName;
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch(IOException e) {
            throw new XiaoXiaException(ResultCode.ERROR, "上传文件失败");
        }
        //上传文件
        ossClient.putObject(bucketName, objectName, inputStream);
        //关闭Oss对象
        ossClient.shutdown();
        return objectName;
    }

    //上传文件接口
    @Override
    public String uploadFile(MultipartFile file) {
        //设置上传参数
        String endpoint = ConstantPropertiesUtils.PRIVATE_END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.PRIVATE_BUCKET_NAME;
        String objectName = upload(file, endpoint, accessKeyId, accessKeySecret, bucketName);
        return objectName;
    }

    //下载文件
    @Override
    public void downloadFile(String fileSourceId , String name , HttpServletResponse response) {
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
            name = new String(name.getBytes(StandardCharsets.UTF_8) , "ISO8859-1");
        } catch(UnsupportedEncodingException e) {
            name = UUIDUtil.getUUID() + "." + suffix;
        }
        response.setHeader("content-disposition" , "attachment;filename=" + name);

        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
        } catch(IOException e) {
            throw new XiaoXiaException(ResultCode.ERROR , "下载文件失败");
        }

        byte[] bytes = new byte[1024];
        int len = -1;
        try {
            while((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes , 0 , len);
            }
        }catch(IOException e){
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
        ossClient.shutdown();
    }

    //计算文件大小
    @Override
    public String getFileLength(MultipartFile file) {
        //转储临时文件
        File dfile = null;
        try {
            dfile = File.createTempFile("prefix", "_" + file.getOriginalFilename());
            file.transferTo(dfile);
        } catch (IOException e) {
            throw new RuntimeException();
        }

        FileChannel fc = null;
        String size = "";
        try {
            FileInputStream fis = new FileInputStream(dfile);
            fc = fis.getChannel();
            BigDecimal fileSize = new BigDecimal(fc.size());
            size = fileSize.divide(new BigDecimal(1048576), 2, RoundingMode.HALF_UP) + "";
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            if (null != fc){
                try{
                    fc.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        dfile.delete();
        return size;
    }

}
