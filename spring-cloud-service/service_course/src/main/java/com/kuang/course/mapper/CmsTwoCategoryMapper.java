package com.kuang.course.mapper;

import com.kuang.course.entity.CmsTwoCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.course.entity.vo.IndexCategoryVo;
import com.kuang.course.entity.vo.SlideTitleVo;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-08
 */
public interface CmsTwoCategoryMapper extends BaseMapper<CmsTwoCategory> {

    //通过一级分类查找侧边栏,通过sort升序
    List<SlideTitleVo> findSlideTitleByOcId(String ocId);

    //查找二级分类,通过一级分类id查找
    List<IndexCategoryVo> findIndexCategoryVoByOcId(String ocId);
}
