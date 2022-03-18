package com.kuang.vip.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.entity.RightRedis;
import com.kuang.springcloud.utils.VipUtils;
import com.kuang.vip.entity.Rights;
import com.kuang.vip.entity.vo.RightsVo;
import com.kuang.vip.mapper.RightsMapper;
import com.kuang.vip.service.CacheService;
import com.kuang.vip.service.RightsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-07
 */
@Service
@Slf4j
public class RightsServiceImpl extends ServiceImpl<RightsMapper, Rights> implements RightsService {

    @Resource
    private CacheService cacheService;


    //查询出vip权益,非普通会员权益
    @Cacheable(value = "vipRightList")
    @Override
    public List<RightsVo> findVipRight() {
        log.info("查询出vip权益,非普通会员权益,即价格不为0");
        return baseMapper.findVipRight();
    }


    //查询出指定用户权益
    @Override
    public RightRedis findRightByUserId(String userId) {
        log.info("查询指定用户权益,用户id:" + userId);
        return VipUtils.getUserRightRedis(userId,
                cacheService.CacheAllMembersRedisTreeMap(),
                cacheService.CacheAllRightRedisTreeMap(),
                cacheService.CacheNotVipRight());
    }


}
