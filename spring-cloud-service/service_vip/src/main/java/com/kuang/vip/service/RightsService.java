package com.kuang.vip.service;

import com.kuang.springcloud.entity.RightRedis;
import com.kuang.vip.entity.Rights;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.vip.entity.vo.RightsVo;

import java.util.List;
import java.util.TreeMap;

/**
 * @author Xiaozhang
 * @since 2022-02-07
 */
public interface RightsService extends IService<Rights> {

    //查询出vip权益,非普通会员权益
    List<RightsVo> findVipRight();

    //查询指定用户权益
    RightRedis findRightByUserId(String userId);
}
