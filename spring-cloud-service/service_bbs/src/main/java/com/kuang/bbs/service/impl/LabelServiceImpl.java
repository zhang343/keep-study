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

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@Service
@Slf4j
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label> implements LabelService {

    //查找文章标签
    @Async
    @Override
    public Future<List<String>> findArticleLabel(String articleId) {
        log.info("查找文章标签,文章id:" + articleId);
        QueryWrapper<Label> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        List<Label> labelList = baseMapper.selectList(wrapper);
        List<String> labelNameList = new ArrayList<>();
        for(Label label : labelList){
            labelNameList.add(label.getLabelName());
        }
        return new AsyncResult<>(labelNameList);
    }

    //增加文章标签
    @Async
    @Override
    public void addArticleLabel(String articleId , List<String> labelList) {
        log.info("增加文章标签,文章id:" + articleId + ",标签:" + labelList);
        //先删除文章所有标签
        QueryWrapper<Label> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        baseMapper.delete(wrapper);
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
    @Async
    @Override
    public void deleteArticleLabel(String articleId) {
        log.info("删除文章标签,文章id:" + articleId);
        QueryWrapper<Label> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        baseMapper.delete(wrapper);
    }

    //查询用户标签
    @Override
    public List<String> findUserLabel(String userId) {
        log.info("查询用户标签:" + userId);
        List<Label> labelList = baseMapper.findUserLabel(userId);
        List<String> list = new ArrayList<>();
        for(Label label : labelList){
            list.add(label.getLabelName());
        }
        return list;
    }
}
