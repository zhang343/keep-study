package com.kuang.vip.mapper;

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
}
