package com.kuang.springcloud.Interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@Component
@Slf4j
public class FeignBeforeExecInterceptor implements RequestInterceptor {


    //每次feig远程调用的时候，将当前请求头中的数据token放入
    public void apply(RequestTemplate template) {
        log.info("feign远程调用拦截，我们在feign远程调用的请求头中加入一些数据");
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //在定时任务和rabbitmq中进行远程调用，此时是没有requestAttributes
        if(requestAttributes == null){
            return;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        // 拿到原始请求头数据
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            // 同步
            template.header("token", token);
        }
    }
}

