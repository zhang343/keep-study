package com.kuang.vip.mapper;

import com.kuang.vip.entity.MoneyK;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.vip.entity.vo.MoneyKVo;

import java.util.List;


public interface MoneyKMapper extends BaseMapper<MoneyK> {

    //查询所有人民币兑换k币的
    List<MoneyKVo> findAll();
}
