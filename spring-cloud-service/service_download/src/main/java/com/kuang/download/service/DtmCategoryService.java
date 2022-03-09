package com.kuang.download.service;

import com.kuang.download.entity.DtmCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface DtmCategoryService extends IService<DtmCategory> {

    //查询出所有分类
    List<DtmCategory> findAll();
    //增加分类,这里没有考虑分类重名
    void addCategory(String categoryName);
    //删除分类,如果该分类下面有文件,则不删除
    void deleteCategory(String id);
    //修改分类名称,通过分类id
    void updateCategory(String id , String categoryName);
}
