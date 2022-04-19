package com.kuang.oss.client;

import com.kuang.springcloud.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(
        name = "service-download" ,
        fallbackFactory = DownloadClientFactory.class
)
public interface DownloadClient {

    @GetMapping("/inside/file/findFileNameAndPriceById")
    R findFileNameAndPriceById(@RequestParam("id") String id);
}
