package com.kuang.vip.service;

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

    //查询出非vip权益
    Rights findNotVipRight();

    //查询出所有权益
    List<Rights> findAllRight();

    //设置所有权益,以权益vipId为key
    TreeMap<String , Object> findAllRightTreeMap();

    //查出用户对应课程打折数量
    double findUserCourseDiscount(String userId);

    //查询指定用户权益
    Rights findRightByUserId(String userId);
}
