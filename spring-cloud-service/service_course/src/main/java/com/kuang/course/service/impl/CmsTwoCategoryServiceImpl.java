package com.kuang.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.course.entity.CmsTwoCategory;
import com.kuang.course.entity.vo.IndexCategoryVo;
import com.kuang.course.entity.vo.SlideTitleVo;
import com.kuang.course.mapper.CmsTwoCategoryMapper;
import com.kuang.course.service.CmsTwoCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class CmsTwoCategoryServiceImpl extends ServiceImpl<CmsTwoCategoryMapper, CmsTwoCategory> implements CmsTwoCategoryService {


    //查询侧边栏，通过一级分类id
    @Cacheable(value = "slideTitleList")
    @Override
    public List<SlideTitleVo> findSlideTitleByOcId(String ocId) {
        return baseMapper.findSlideTitleByOcId(ocId);
    }

    //通过一级分类id进行查找下属二级分类
    @Cacheable(value = "indexCategoryVoList")
    @Override
    public List<IndexCategoryVo> findIndexCategoryVoByOcId(String ocId) {
        return baseMapper.findIndexCategoryVoByOcId(ocId);
    }



}
