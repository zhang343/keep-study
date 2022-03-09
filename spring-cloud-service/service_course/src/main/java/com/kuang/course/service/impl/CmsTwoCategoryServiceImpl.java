package com.kuang.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.course.entity.CmsTwoCategory;
import com.kuang.course.entity.vo.IndexCategoryVo;
import com.kuang.course.entity.vo.IndexCourseVo;
import com.kuang.course.entity.vo.SlideTitleVo;
import com.kuang.course.mapper.CmsCourseMapper;
import com.kuang.course.mapper.CmsTwoCategoryMapper;
import com.kuang.course.service.CmsTwoCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
@Service
@Slf4j
public class CmsTwoCategoryServiceImpl extends ServiceImpl<CmsTwoCategoryMapper, CmsTwoCategory> implements CmsTwoCategoryService {


    //查询侧边栏，通过一级分类id
    @Cacheable(value = "slideTitleList")
    @Override
    public List<SlideTitleVo> findSlideTitleByOcId(String ocId) {
        log.info("查询侧边栏,通过一级id:" + ocId);
        return baseMapper.findSlideTitleByOcId(ocId);
    }

    //通过一级分类id进行查找下属二级分类
    @Cacheable(value = "indexCategoryVoList")
    @Override
    public List<IndexCategoryVo> findIndexCategoryVoByOcId(String ocId) {
        log.info("查找二级分类通过一级分类id查找:" + ocId);
        return baseMapper.findIndexCategoryVoByOcId(ocId);
    }



}
