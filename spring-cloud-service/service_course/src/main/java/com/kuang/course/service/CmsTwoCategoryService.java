package com.kuang.course.service;

import com.kuang.course.entity.CmsTwoCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.course.entity.vo.IndexCategoryVo;
import com.kuang.course.entity.vo.SlideTitleVo;

import java.util.List;


public interface CmsTwoCategoryService extends IService<CmsTwoCategory> {

    //查询侧边栏，通过一级分类id
    List<SlideTitleVo> findSlideTitleByOcId(String ocId);

    //通过一级分类id进行查找下属二级分类和课程
    List<IndexCategoryVo> findIndexCategoryVoByOcId(String ocId);
}
