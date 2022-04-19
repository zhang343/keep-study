package com.kuang.vip.service.impl;

import com.kuang.vip.entity.MoneyK;
import com.kuang.vip.entity.vo.MoneyKVo;
import com.kuang.vip.mapper.MoneyKMapper;
import com.kuang.vip.service.MoneyKService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MoneyKServiceImpl extends ServiceImpl<MoneyKMapper, MoneyK> implements MoneyKService {

    //查询所有人民币兑换k币的
    @Cacheable(value = "moneyKVoList")
    @Override
    public List<MoneyKVo> findAll() {
        return baseMapper.findAll();
    }
}
