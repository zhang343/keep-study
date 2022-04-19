package com.kuang.bbs.mapper;

import com.kuang.bbs.entity.Label;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


public interface LabelMapper extends BaseMapper<Label> {

    //查询用户标签,这里只是返回标签名
    List<String> findUserLabel(String userId);

    //查找文章标签,这里只是返回标签名
    List<String> findArticleLabel(String articleId);
}
