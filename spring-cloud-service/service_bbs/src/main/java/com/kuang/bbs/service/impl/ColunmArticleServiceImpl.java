package com.kuang.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.bbs.client.UcenterClient;
import com.kuang.bbs.client.VipClient;
import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.ArticleRight;
import com.kuang.bbs.entity.Column;
import com.kuang.bbs.entity.ColunmArticle;
import com.kuang.bbs.entity.vo.ColumnArticleVo;
import com.kuang.bbs.entity.vo.PulishColumnArticleVo;
import com.kuang.bbs.entity.vo.UpdateColumnArticleVo;
import com.kuang.bbs.mapper.ArticleMapper;
import com.kuang.bbs.mapper.ArticleRightMapper;
import com.kuang.bbs.mapper.ColunmArticleMapper;
import com.kuang.bbs.service.ArticleRightService;
import com.kuang.bbs.service.ColumnService;
import com.kuang.bbs.service.ColunmArticleService;
import com.kuang.springcloud.entity.RightRedis;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.springcloud.utils.VipUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;


@Service
public class ColunmArticleServiceImpl extends ServiceImpl<ColunmArticleMapper, ColunmArticle> implements ColunmArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleRightService articleRightService;

    @Resource
    private ColumnService columnService;

    //查询专栏文章
    @Override
    public List<ColumnArticleVo> findColumnArticle(String columnId) {
        return baseMapper.findColumnArticle(columnId);
    }

    //删除专栏文章
    @Transactional
    @Override
    public void deleteArticle(String userId, String articleId, String cloumnId) {

        //删除文章具体数据，这里我们就校验了文章是否为用户所有
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.eq("user_id" , userId);
        articleQueryWrapper.eq("id" , articleId);
        int i = articleMapper.delete(articleQueryWrapper);
        if(i != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "删除失败");
        }

        //删除专栏文章列表数据
        QueryWrapper<ColunmArticle> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        wrapper.eq("column_id" , cloumnId);
        int delete = baseMapper.delete(wrapper);
        if(delete != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "删除失败");
        }

    }

    //发布专栏文章
    @Transactional
    @Override
    public Article publishArticle(PulishColumnArticleVo pulishColumnArticleVo,
                                  String conlumnId ,
                                  String userId ,
                                  String nickname ,
                                  String avatar) {

        //校验专栏是否为用户所有
        Column byId = columnService.getById(conlumnId);
        if(byId == null || !byId.getUserId().equals(userId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }

        //插入文章
        Article article = new Article();
        BeanUtils.copyProperties(pulishColumnArticleVo , article);
        article.setUserId(userId);
        article.setNickname(nickname);
        article.setAvatar(avatar);
        //设置为专栏文章
        article.setIsColumnArticle(true);
        int insert = articleMapper.insert(article);
        if(insert != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "发布失败");
        }

        //插入专栏文章
        ColunmArticle colunmArticle = new ColunmArticle();
        colunmArticle.setColumnId(conlumnId);
        colunmArticle.setArticleId(article.getId());
        int insert1 = baseMapper.insert(colunmArticle);
        if(insert1 != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "发布失败");
        }


        //如果文章同步到江湖，就需要进行相应的处理
        Boolean isBbs = pulishColumnArticleVo.getIsBbs();
        if(isBbs != null && isBbs){
            //修改用户当日文章权益
            articleRightService.updateArticleRight(userId);
        }
        return article;
    }

    //进行数据验证，看用户是否可以访问该专栏文章
    @Async
    @Override
    public Future<Boolean> checkUserAbility(String userId, String columnId, String articleId) {

        //校验该文章是否属于这个专栏
        QueryWrapper<ColunmArticle> wrapper = new QueryWrapper<>();
        wrapper.eq("column_id" , columnId);
        wrapper.eq("article_id" , articleId);
        Integer integer = baseMapper.selectCount(wrapper);
        if(integer == 0){
            return new AsyncResult<>(false);
        }

        //校验是否可以访问该专栏
        return columnService.checkUserAbility(userId, columnId);
    }

    //检查该文章是否属于用户，属于该专栏
    @Async
    @Override
    public Future<Boolean> checkUserColumnArticle(String userId, String columnId, String articleId) {
        Column column = columnService.getById(columnId);
        //专栏是否存在和专栏是否为该用户所有者,不符合返回false
        if(column == null || !column.getUserId().equals(userId)){
            return new AsyncResult<>(false);
        }


        //检测该专栏是否包含该文章,不包含返回false
        QueryWrapper<ColunmArticle> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        wrapper.eq("column_id" , columnId);
        Integer integer = baseMapper.selectCount(wrapper);
        if(integer == 0){
            return new AsyncResult<>(false);
        }

        //检测是否为文章所有者,不是文章所有者返回false
        QueryWrapper<Article> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("id" , articleId);
        wrapper1.eq("user_id" , userId);
        Integer integer1 = articleMapper.selectCount(wrapper1);
        if(integer1 == 0){
            return new AsyncResult<>(false);
        }

        return new AsyncResult<>(true);
    }

    //修改专栏文章
    @Transactional
    @Override
    public void updateCloumArticle(UpdateColumnArticleVo updateColumnArticleVo , String userId, String columnId, String articleId) {
        Future<Boolean> booleanFuture = checkUserColumnArticle(userId, columnId, articleId);

        Article article = articleMapper.selectById(articleId);
        if(!article.getIsBbs() && updateColumnArticleVo.getIsBbs() != null && updateColumnArticleVo.getIsBbs()){
            if(article.getIsViolationArticle()){
                throw new XiaoXiaException(ResultCode.ERROR , "修改失败");
            }
            //修改用户当日文章权益
            articleRightService.updateArticleRight(userId);
        }
        Article updateArticle = new Article();
        BeanUtils.copyProperties(updateColumnArticleVo , updateArticle);
        updateArticle.setId(updateColumnArticleVo.getArticleId());
        int i = articleMapper.updateById(updateArticle);
        if(i != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "修改失败");
        }

        boolean flag = false;
        try {
            flag = booleanFuture.get();
        }catch(Exception e){
            e.printStackTrace();
        }
        if(!flag){
            throw new XiaoXiaException(ResultCode.ERROR , "修改失败");
        }
    }
}
