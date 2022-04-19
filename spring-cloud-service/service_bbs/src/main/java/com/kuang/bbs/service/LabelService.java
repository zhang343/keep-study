package com.kuang.bbs.service;

import com.kuang.bbs.entity.Label;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.concurrent.Future;


public interface LabelService extends IService<Label> {

    //查找文章标签
    List<String> findArticleLabel(String articleId);

    //增加文章标签
    void addArticleLabel(String articleId , List<String> labelList);

    //删除文章标签
    void deleteArticleLabel(String articleId);

    //查询用户标签
    List<String> findUserLabel(String userId);
}
