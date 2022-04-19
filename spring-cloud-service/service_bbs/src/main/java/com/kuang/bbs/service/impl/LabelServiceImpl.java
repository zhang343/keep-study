package com.kuang.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.bbs.entity.Label;
import com.kuang.bbs.mapper.LabelMapper;
import com.kuang.bbs.service.LabelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;


@Service
@Slf4j
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label> implements LabelService {

    //查找文章标签
    @Override
    public List<String> findArticleLabel(String articleId) {
        return baseMapper.findArticleLabel(articleId);
    }

    //增加文章标签
    @Async
    @Override
    public void addArticleLabel(String articleId , List<String> labelList) {
        //删除文章所有标签
        deleteArticleLabel(articleId);

        //去除重复标签
        Set<String> labelSet = new HashSet<>(labelList);
        List<Label> list = new ArrayList<>();
        for(String labelName : labelSet){
            Label label = new Label();
            label.setArticleId(articleId);
            label.setLabelName(labelName);
            list.add(label);
        }

        if(list.size() != 0){
            //插入
            saveBatch(list);
        }
    }

    //删除文章标签
    @Override
    public void deleteArticleLabel(String articleId) {
        QueryWrapper<Label> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        baseMapper.delete(wrapper);
    }

    //查询用户标签
    @Override
    public List<String> findUserLabel(String userId) {
        return baseMapper.findUserLabel(userId);
    }
}
