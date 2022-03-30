package com.kuang.bbs.service;

import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.ColunmArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.bbs.entity.vo.ColumnArticleVo;
import com.kuang.bbs.entity.vo.PulishColumnArticleVo;
import com.kuang.bbs.entity.vo.UpdateColumnArticleVo;

import java.util.List;
import java.util.concurrent.Future;


public interface ColunmArticleService extends IService<ColunmArticle> {

    //查询专栏文章
    List<ColumnArticleVo> findColumnArticle(String columnId);

    //删除专栏文章
    void deleteArticle(String userId, String articleId, String cloumnId);

    //发布专栏文章
    Article publishArticle(PulishColumnArticleVo pulishColumnArticleVo, String conlumnId , String userId , String nickname , String avatar);

    //进行数据验证，看用户是否可以访问该专栏文章
    Future<Boolean> checkUserAbility(String userId, String columnId, String articleId);

    //检查该文章是否属于用户，属于该专栏
    Future<Boolean> checkUserColumnArticle(String userId, String columnId, String articleId);

    //修改专栏文章
    void updateCloumArticle(UpdateColumnArticleVo updateColumnArticleVo);
}
