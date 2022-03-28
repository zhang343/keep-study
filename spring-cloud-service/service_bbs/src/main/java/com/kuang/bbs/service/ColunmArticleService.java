package com.kuang.bbs.service;

import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.ColunmArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.bbs.entity.vo.ColumnArticleVo;
import com.kuang.bbs.entity.vo.PulishColumnArticleVo;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-03-27
 */
public interface ColunmArticleService extends IService<ColunmArticle> {

    //查询专栏文章
    List<ColumnArticleVo> findColumnArticle(String columnId);

    //删除专栏文章
    void deleteArticle(String userId, String articleId, String cloumnId);

    //发布专栏文章
    Article publishArticle(PulishColumnArticleVo pulishColumnArticleVo, String conlumnId , String userId , String nickname , String avatar);
}
