package com.kuang.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.course.entity.CmsOneCategory;
import com.kuang.course.entity.vo.OneCategoryVo;
import com.kuang.course.mapper.CmsOneCategoryMapper;
import com.kuang.course.service.CmsOneCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
@Service
@Slf4j
public class CmsOneCategoryServiceImpl extends ServiceImpl<CmsOneCategoryMapper, CmsOneCategory> implements CmsOneCategoryService {

    //查询所有一级分类
    @Cacheable(value = "oneCategoryList")
    @Override
    public List<OneCategoryVo> findAllFirstLevel() {
        log.info("查询所有一级分类");
        return baseMapper.findAllFirstLevel();
    }
}
