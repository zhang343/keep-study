package com.kuang.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.bbs.client.UcenterClient;
import com.kuang.bbs.client.VipClient;
import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.ArticleRight;
import com.kuang.bbs.entity.ColunmArticle;
import com.kuang.bbs.entity.vo.ColumnArticleVo;
import com.kuang.bbs.entity.vo.PulishColumnArticleVo;
import com.kuang.bbs.mapper.ArticleMapper;
import com.kuang.bbs.mapper.ArticleRightMapper;
import com.kuang.bbs.mapper.ColunmArticleMapper;
import com.kuang.bbs.service.ColunmArticleService;
import com.kuang.springcloud.entity.RightRedis;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.springcloud.utils.VipUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-03-27
 */
@Service
public class ColunmArticleServiceImpl extends ServiceImpl<ColunmArticleMapper, ColunmArticle> implements ColunmArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleRightMapper articleRightMapper;

    @Resource
    private VipClient vipClient;

    @Resource
    private UcenterClient ucenterClient;

    //查询专栏文章
    @Override
    public List<ColumnArticleVo> findColumnArticle(String columnId) {
        return baseMapper.findColumnArticle(columnId);
    }

    //删除专栏文章
    @Transactional
    @Override
    public void deleteArticle(String userId, String articleId, String cloumnId) {
        QueryWrapper<ColunmArticle> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id" , articleId);
        wrapper.eq("column_id" , cloumnId);
        int delete = baseMapper.delete(wrapper);
        if(delete != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "删除失败");
        }

        int i = articleMapper.deleteById(articleId);
        if(i != 1){
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
        if(isBbs){
            //如果文章是同步到江湖的
            RightRedis userRightRedis = VipUtils.getUserRightRedis(userId);
            if(userRightRedis == null){
                R rightRedisByUserId = vipClient.findRightRedisByUserId(userId);
                if(!rightRedisByUserId.getSuccess()){
                    throw new XiaoXiaException(ResultCode.ERROR , "发布文章失败");
                }
                userRightRedis = (RightRedis) rightRedisByUserId.getData().get("rightRedis");
            }

            QueryWrapper<ArticleRight> wrapper = new QueryWrapper<>();
            wrapper.select("id" , "money" , "article_number" , "version");
            wrapper.eq("user_id" , userId);
            ArticleRight articleRight = articleRightMapper.selectOne(wrapper);
            if(articleRight.getArticleNumber() + 1 > userRightRedis.getArticleNumber()){
                throw new XiaoXiaException(ResultCode.ERROR , "今日文章发布江湖数量已满");
            }
            articleRight.setArticleNumber(articleRight.getArticleNumber() + 1);

            if(articleRight.getMoney() + 10 > userRightRedis.getMoney()){
                articleRight.setMoney(null);
                int i = articleRightMapper.updateById(articleRight);
                if(i != 1){
                    throw new XiaoXiaException(ResultCode.ERROR , "发布文章失败");
                }
            }else {
                articleRight.setMoney(articleRight.getMoney() + 10);
                int i = articleRightMapper.updateById(articleRight);
                if(i != 1){
                    throw new XiaoXiaException(ResultCode.ERROR , "发布文章失败");
                }
                //不强制一样
                ucenterClient.add(10);
            }
        }
        return article;
    }
}
