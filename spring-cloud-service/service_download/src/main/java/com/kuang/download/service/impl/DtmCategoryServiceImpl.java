package com.kuang.download.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.download.entity.DtmCategory;
import com.kuang.download.entity.DtmFile;
import com.kuang.download.entity.vo.DtmCategoryVo;
import com.kuang.download.mapper.DtmCategoryMapper;
import com.kuang.download.mapper.DtmFileMapper;
import com.kuang.download.service.DtmCategoryService;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@Service
@Slf4j
public class DtmCategoryServiceImpl extends ServiceImpl<DtmCategoryMapper, DtmCategory> implements DtmCategoryService {


    @Resource
    private DtmFileMapper dtmFileMapper;

    //查询出所有分类
    @Cacheable(value = "dtmCategoryList")
    @Override
    public List<DtmCategoryVo> findAll() {
        log.info("查询所有文件分类,这里查找id和category_name");
        return baseMapper.findAll();
    }

    //增加分类,这里没有考虑分类重名
    @Override
    public void addCategory(String categoryName) {
        log.info("开始增加分类,分类名:" + categoryName);
        DtmCategory dtmCategory = new DtmCategory();
        dtmCategory.setCategoryName(categoryName);
        int num = baseMapper.insert(dtmCategory);
        if(num != 1){
            log.error("增加分类失败,分类名:" + categoryName);
            throw new XiaoXiaException(ResultCode.ERROR , "增加分类失败");
        }
    }

    //删除分类,如果该分类下面有文件,则不删除
    @Override
    public void deleteCategory(String id) {
        log.info("删除分类,分类id是：" + id);
        QueryWrapper<DtmFile> dtmFileWrapper = new QueryWrapper<>();
        dtmFileWrapper.eq("category_id" , id);
        Integer integer = dtmFileMapper.selectCount(dtmFileWrapper);
        if(integer != 0){
            log.warn("删除分类失败,该分类下面有文件,分类id：" + id);
            throw new XiaoXiaException(ResultCode.ERROR , "分类下面有文件，不能删除该分类");
        }
        int i = baseMapper.deleteById(id);
        if(i != 1){
            log.error("删除分类失败,分类id：" + id);
            throw new XiaoXiaException(ResultCode.ERROR , "删除分类失败");
        }
    }

    //修改分类名称,通过分类id
    @Override
    public void updateCategory(String id, String categoryName) {
        log.info("更新分类id:" + id + ",将其分类名改为:" + categoryName);
        QueryWrapper<DtmCategory> wrapper = new QueryWrapper<>();
        wrapper.select("id" , "version");
        wrapper.eq("id" , id);
        DtmCategory dtmCategory = baseMapper.selectById(wrapper);
        if(dtmCategory == null){
            throw new XiaoXiaException(ResultCode.ERROR , "没有该分类");
        }
        dtmCategory.setCategoryName(categoryName);
        int num = baseMapper.updateById(dtmCategory);
        if(num != 1){
            log.info("更新分类id:" + id + ",将其分类名改为:" + categoryName + "失败");
            throw new XiaoXiaException(ResultCode.ERROR , "更新分类名失败");
        }
    }
}
