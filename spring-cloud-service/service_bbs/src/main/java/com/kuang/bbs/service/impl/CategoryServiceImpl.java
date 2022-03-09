package com.kuang.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.bbs.entity.Category;
import com.kuang.bbs.mapper.CategoryMapper;
import com.kuang.bbs.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    //查询所有分类
    @Cacheable(value = "articleCategoryList")
    @Override
    public List<Category> findAllCategory() {
        log.info("查询所有文章分类");
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.select("id" , "category_name");
        return baseMapper.selectList(wrapper);
    }

    //查询指定的分类
    @Override
    public Category findCategoryByCategoryId(String categoryId) {
        log.info("查询指定的分类");
        List<Category> allCategory = findAllCategory();
        for(Category category : allCategory){
            if(category.getId().equals(categoryId)){
                return category;
            }
        }
        return null;
    }
}
