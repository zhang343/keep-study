package com.kuang.vip.service.impl;

import com.kuang.vip.entity.Order;
import com.kuang.vip.mapper.OrderMapper;
import com.kuang.vip.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
