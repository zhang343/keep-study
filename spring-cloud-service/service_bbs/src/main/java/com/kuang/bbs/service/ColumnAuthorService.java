package com.kuang.bbs.service;

import com.kuang.bbs.entity.ColumnAuthor;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.bbs.entity.vo.ColumnAuthorVo;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-03-25
 */
public interface ColumnAuthorService extends IService<ColumnAuthor> {

    //查询专栏作者
    List<ColumnAuthorVo> findColumnAuthorList(String columnId);
}
