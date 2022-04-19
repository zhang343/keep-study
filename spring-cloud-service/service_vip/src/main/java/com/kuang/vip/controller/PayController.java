package com.kuang.vip.controller;

import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.vip.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/vm/order")
@Slf4j
public class PayController {

    @Resource
    private PayService payService;


    //充值k币，生成支付账单
    @PostMapping("addOrder")
    public String addOrder(String id , HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(id) || userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法操作");
        }
        return payService.addOrder(id, userId);
    }


    //支付宝同步通知方法
    @GetMapping("returnUrl")
    public R returnUrl(){
        log.info("支付宝同步通知方法returnUrl执行");
        return R.ok();
    }

    //支付宝支付异步通知执行方法
    @PostMapping("notifyUrl")
    public String notifyUrl(HttpServletRequest request){
        log.info("支付宝异步调用notifyUrl方法,通知后台支付结果");

        //从支付宝回调的request域中取值
        //获取支付宝返回的参数集合
        Map<String, String[]> aliParams = request.getParameterMap();
        //用以存放转化后的参数集合
        Map<String, String> conversionParams = new HashMap<>();
        for(String key : aliParams.keySet()) {
            String[] values = aliParams.get(key);
            String valueStr = "";
            for(int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            conversionParams.put(key, valueStr);
        }
        log.info("支付宝异步调用notifyUrl方法返回参数集合:" + conversionParams);
        return payService.notify(conversionParams);
    }


    //用户主动查询支付状态
    @PostMapping("checkAlipay")
    public R checkAlipay(String outTradeNo){
        payService.checkAlipay(outTradeNo);
        return R.ok();
    }
}
