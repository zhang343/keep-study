package com.kuang.bbs.service.impl;

import com.kuang.bbs.entity.ColunmArticle;
import com.kuang.bbs.entity.vo.ColumnArticleVo;
import com.kuang.bbs.mapper.ColunmArticleMapper;
import com.kuang.bbs.service.ColunmArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-03-27
 */
@Service
public class ColunmArticleServiceImpl extends ServiceImpl<ColunmArticleMapper, ColunmArticle> implements ColunmArticleService {

    //查询专栏文章
    @Override
    public List<ColumnArticleVo> findColumnArticle(String columnId) {
        return baseMapper.findColumnArticle(columnId);
    }
}
