package com.kuang.springcloud.exceptionhandler;


import com.kuang.springcloud.utils.ExceptionUtil;
import com.kuang.springcloud.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author XiaoZhang
 * @date 2022/1/22 14:32
 * 异常处理器
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    //全局异常统一处理
    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public R error(Exception e){
        log.error(ExceptionUtil.getMessage(e));
        return R.error();
    }

    //网站特定异常处理
    @ExceptionHandler(value = {XiaoXiaException.class})
    @ResponseBody
    public R error(XiaoXiaException e){
        log.error(ExceptionUtil.getMessage(e));
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
