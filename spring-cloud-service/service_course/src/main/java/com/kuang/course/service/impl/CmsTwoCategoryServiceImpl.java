package com.kuang.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.course.entity.CmsTwoCategory;
import com.kuang.course.entity.vo.IndexCategoryVo;
import com.kuang.course.entity.vo.IndexCourseVo;
import com.kuang.course.entity.vo.SlideTitleVo;
import com.kuang.course.mapper.CmsTwoCategoryMapper;
import com.kuang.course.service.CmsTwoCategoryService;
import com.kuang.springcloud.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    //通过一级分类id进行查找下属二级分类和课程
    @Override
    public List<IndexCategoryVo> findIndexCategoryVoByOcId(String ocId) {
        List<IndexCategoryVo> indexCategoryVoByOcId = null;
        Object value = RedisUtils.getValue("indexCategoryVoList" + ocId);
        if(value == null){
            indexCategoryVoByOcId = baseMapper.findIndexCategoryVoByOcId(ocId);
            RedisUtils.setValueTimeout("indexCategoryVoList" + ocId , indexCategoryVoByOcId , 6);
        }else {
            indexCategoryVoByOcId = (List<IndexCategoryVo>)value;
        }
        for(IndexCategoryVo indexCategoryVo : indexCategoryVoByOcId){
            List<IndexCourseVo> courseList = indexCategoryVo.getCourseList();
            for(IndexCourseVo courseVo : courseList){
                long setSize = RedisUtils.getSetSize(courseVo.getId());
                courseVo.setViews(setSize + courseVo.getViews());
            }
        }
        return indexCategoryVoByOcId;
    }
}
