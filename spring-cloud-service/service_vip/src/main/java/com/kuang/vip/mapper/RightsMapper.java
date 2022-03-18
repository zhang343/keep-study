package com.kuang.vip.mapper;

import com.kuang.springcloud.entity.RightRedis;
import com.kuang.vip.entity.Rights;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.vip.entity.vo.RightsVo;

import java.util.List;

/**
 *
 * @author Xiaozhang
 * @since 2022-02-07
 */
public interface RightsMapper extends BaseMapper<Rights> {

    //查询出vip权益,非普通会员权益
    List<RightsVo> findVipRight();

    //查询出非vip权益
    RightRedis findNotVipRight();

    //查询出所有权益
    List<RightRedis> findAllRight();
}
