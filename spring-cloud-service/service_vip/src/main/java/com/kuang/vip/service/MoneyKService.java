package com.kuang.vip.service;

import com.kuang.vip.entity.MoneyK;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.vip.entity.vo.MoneyKVo;

import java.util.List;


public interface MoneyKService extends IService<MoneyK> {

    //查询所有人民币兑换k币的
    List<MoneyKVo> findAll();
}
