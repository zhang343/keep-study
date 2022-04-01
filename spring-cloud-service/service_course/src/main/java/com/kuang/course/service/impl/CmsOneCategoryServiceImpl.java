package com.kuang.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.course.entity.CmsOneCategory;
import com.kuang.course.entity.vo.OneCategoryVo;
import com.kuang.course.mapper.CmsOneCategoryMapper;
import com.kuang.course.service.CmsOneCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class CmsOneCategoryServiceImpl extends ServiceImpl<CmsOneCategoryMapper, CmsOneCategory> implements CmsOneCategoryService {

    //查询所有一级分类
    @Cacheable(value = "oneCategoryList")
    @Override
    public List<OneCategoryVo> findAllFirstLevel() {
        return baseMapper.findAllFirstLevel();
    }
}
