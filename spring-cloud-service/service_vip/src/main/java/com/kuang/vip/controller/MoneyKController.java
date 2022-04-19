package com.kuang.vip.controller;


import com.kuang.springcloud.utils.R;
import com.kuang.vip.entity.vo.MoneyKVo;
import com.kuang.vip.service.MoneyKService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/vm/moneyk")
public class MoneyKController {

    @Resource
    private MoneyKService moneyKService;

    //查询所有人民币兑换k币的
    @GetMapping("findAll")
    public R findAll(){
        List<MoneyKVo> moneyKVos = moneyKService.findAll();
        return R.ok().data("moneyKVos" , moneyKVos);
    }

}

