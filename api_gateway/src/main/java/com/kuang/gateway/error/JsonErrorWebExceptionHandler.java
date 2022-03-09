package com.kuang.gateway.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XiaoZhang
 * @date 2022/2/3 16:20
 * 异常处理类
 */
@Slf4j
public class JsonErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    public JsonErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                      ResourceProperties resourceProperties,
                                      ErrorProperties errorProperties,
                                      ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }


    /**
     * 获取异常属性
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        log.error("此次访问路径：" + request.path() + "，访问出现异常，进行异常数据处理");
        Map<String, Object> map = new HashMap<>();
        map.put("success" , false);
        map.put("code", 20004);
        map.put("message", "服务器开小差了，请稍后再试");
        map.put("data", new HashMap<String , Object>());
        log.error("异常数据处理结束，返回数据为：" + map);
        return map;
    }

    /**
     * 指定响应处理方法为JSON处理的方法
     * @param errorAttributes
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        log.error("设置异常响应处理方法，该方法为：protected Mono<ServerResponse> renderErrorResponse(ServerRequest request)");
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }


    /**
     * 设置相应的响应状态码
     * @param errorAttributes
     */
    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        log.error("设置异常处理响应状态码，状态码为：" + HttpStatus.OK.value());
        return HttpStatus.OK.value();
    }

}