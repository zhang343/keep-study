package com.kuang.oss.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author XiaoZhang
 * @date 2022/2/6 11:32
 * 远程调用service-download服务类
 */
@FeignClient(
        name = "service-download" ,
        fallbackFactory = DownloadClientFactory.class
)
public interface DownloadClient {

    @GetMapping("/dtm/file/findFileNameAndPriceById")
    R findFileNameAndPriceById(@RequestParam("id") String id);
}
