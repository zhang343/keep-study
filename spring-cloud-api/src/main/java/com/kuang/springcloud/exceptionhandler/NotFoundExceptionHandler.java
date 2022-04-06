package com.kuang.springcloud.exceptionhandler;

import com.kuang.springcloud.utils.R;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
public class NotFoundExceptionHandler implements ErrorController {


    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = {"/error"})
    @ResponseBody
    public Object error(HttpServletRequest request) {
        return R.error().message("没有这个接口哦");
    }

}


