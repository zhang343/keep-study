package com.kuang.bbs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.bbs.client.UcenterClient;
import com.kuang.bbs.client.VipClient;
import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.Category;
import com.kuang.bbs.entity.vo.*;
import com.kuang.bbs.mapper.ArticleMapper;
import com.kuang.bbs.service.*;
import com.kuang.springcloud.entity.InfoFriendFeedVo;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.rabbitmq.MsgProducer;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.RedisUtils;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.springcloud.utils.VipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private UcenterClient ucenterClient;

    @Resource
    private MsgProducer msgProducer;

    @Resource
    private CategoryService categoryService;

    @Resource
    private ArticleRightService articleRightService;

    @Resource
    private CommentService commentService;

    @Resource
    private LabelService labelService;


    //查询系统文章数量
    @Cacheable(value = "articleNumber")
    @Override
    public Integer findArticleNumber() {
        return baseMapper.selectCount(null);
    }

    //查询文章详细数据
    @Override
    public ArticleVo findArticleDetail(String articleId , String userId) {

        //查询文章具体数据
        Article article = baseMapper.selectById(articleId);
        if(article == null){
            throw new XiaoXiaException(ResultCode.ERROR , "该文章不存在");
        }

        //取出文章所有者id
        String articleUserId = article.getUserId();

        //下面进行数据验证
        /*
        对于文章拥有者，只需要检验该文章不能是专栏文章没发布到江湖
        对于另一种来说，检验
        1：不是违规文章
        2：非专栏文章，没有发布
        3：专栏文章没发布到江湖
         */
        //如果是专栏文章，但是没有同步到江湖
        if(article.getIsColumnArticle() && !article.getIsBbs()){
            throw new XiaoXiaException(ResultCode.ERROR , "该文章不存在");
        }

        //如果查询文章的和文章所有者不一致
        if(!articleUserId.equals(userId)){
            //违规不可查
            if(article.getIsViolationArticle()){
                throw new XiaoXiaException(ResultCode.ERROR , "该文章不存在");
            }
            //非专栏文章,但是没发布
            if(!article.getIsColumnArticle() && !article.getIsRelease()){
                throw new XiaoXiaException(ResultCode.ERROR , "该文章不存在");
            }
        }


        ArticleVo articleVo = new ArticleVo();
        //拷贝相应数据
        BeanUtils.copyProperties(article , articleVo);
        //设置分类
        String categoryId = article.getCategoryId();
        Category categoryByCategoryId = categoryService.findCategoryByCategoryId(categoryId);
        articleVo.setCategoryName(categoryByCategoryId.getCategoryName());

        //调整是否是处于发布状态
        if(article.getIsColumnArticle() && article.getIsBbs()){
            articleVo.setIsRelease(true);
        }
        //设置vip等级
        String userVipLevel = VipUtils.getUserVipLevel(articleUserId);
        articleVo.setVipLevel(userVipLevel);
        return articleVo;
    }

    //根据条件查询系统文章总数
    @Override
    public Long findArticleNumber(String categoryId, Boolean isExcellentArticle, String articleNameOrLabelName) {
        return baseMapper.findArticleNumber(categoryId , isExcellentArticle , articleNameOrLabelName);
    }

    //条件分页查询文章
    @Async
    @Override
    public Future<List<IndexArticleVo>> pageArticleCondition(Long current, Long limit, String categoryId, Boolean isExcellentArticle, String articleNameOrLabelName) {

        //条件查询文章
        List<IndexArticleVo> indexArticleVoList = baseMapper.pageArticleCondition((current - 1) * limit , limit , categoryId , isExcellentArticle , articleNameOrLabelName);

        if(current == 1 && limit == 10 && StringUtils.isEmpty(categoryId) && StringUtils.isEmpty(articleNameOrLabelName) && (isExcellentArticle == null || !isExcellentArticle)){
            //说明为首页查询,此时我们查看indexArticleVoList是否有首页文章,如果有,去除
            IndexArticleVo topArticle = null;
            for(IndexArticleVo indexArticleVo : indexArticleVoList){
                if(indexArticleVo.getIsTop()){
                    topArticle = indexArticleVo;
                    break;
                }
            }

            //如果有首页，去除，并加入第一个
            if(topArticle != null){
                indexArticleVoList.remove(topArticle);
                indexArticleVoList.add(0 , topArticle);
            }else {
                //没有首页,去除第一个,并查询首页文章
                topArticle = findTopArticle();
                //如果首页文章存在，去除第一个,
                if(topArticle != null){
                    indexArticleVoList.set(0 , topArticle);
                }
            }
        }
        VipUtils.setVipLevel(indexArticleVoList , indexArticleVoList.get(0));
        return new AsyncResult<>(indexArticleVoList);
    }

    //用户发布文章
    @Transactional
    @Override
    public Article addArticle(ArticleUpdateAndCreateVo articleUpdateAndCreateVo , String avatar , String nickname  , String userId) {
        //进行相关数据封装
        Article article = new Article();
        BeanUtils.copyProperties(articleUpdateAndCreateVo , article);
        article.setId(null);
        article.setUserId(userId);
        article.setAvatar(avatar);
        article.setNickname(nickname);
        //设置保证为非专栏文章
        article.setIsColumnArticle(false);
        int insert = baseMapper.insert(article);
        if(insert != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "发布文章失败");
        }

        //调整用户每日文章权益
        articleRightService.updateArticleRight(userId);
        return article;
    }

    //查询江湖文章以便修改
    @Override
    public ArticleUpdateAndCreateVo findArticleByArticleIdAndUserId(String articleId, String userId) {

        //查询文章
        Article article = baseMapper.selectById(articleId);
        if(article == null){
            throw new XiaoXiaException(ResultCode.ERROR , "该文章不存在");
        }

        //校验是否为文章所有者
        if(!userId.equals(article.getUserId())){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法修改文章");
        }

        //专栏文章，但是没有同步到江湖
        if(article.getIsColumnArticle() && !article.getIsBbs()){
            throw new XiaoXiaException(ResultCode.ERROR , "该文章不存在");
        }

        //拷贝文章数据
        ArticleUpdateAndCreateVo articleUpdateAndCreateVo = new ArticleUpdateAndCreateVo();
        BeanUtils.copyProperties(article , articleUpdateAndCreateVo);
        //设置发布状态
        if(article.getIsColumnArticle() && article.getIsBbs()){
            articleUpdateAndCreateVo.setIsRelease(true);
        }
        return articleUpdateAndCreateVo;
    }

    //用户修改江湖文章
    @Transactional
    @Override
    public void updateArticle(ArticleUpdateAndCreateVo articleUpdateAndCreateVo , String userId) {
        String articleId = articleUpdateAndCreateVo.getId();
        //查询文章
        Article article = baseMapper.selectById(articleId);
        if(article == null){
            throw new XiaoXiaException(ResultCode.ERROR , "该文章不存在");
        }

        //校验是否为文章所有者
        if(!userId.equals(article.getUserId())){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法修改文章");
        }
        //专栏文章，但是没有同步到江湖
        if(article.getIsColumnArticle() && !article.getIsBbs()){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法修改文章");
        }


        Article updateArticle = new Article();
        BeanUtils.copyProperties(articleUpdateAndCreateVo , updateArticle);
        //进行相应的数据判断修改
        //专栏文章，同步到江湖
        if(article.getIsColumnArticle() && article.getIsBbs()){
            updateArticle.setIsRelease(null);
        }
        //非专栏文章，已经发布的
        if(!article.getIsColumnArticle() && article.getIsRelease()){
            updateArticle.setIsRelease(null);
        }

        //这里不用乐观锁,因为这里修改的字段只有用户本人可用修改
        int i = baseMapper.updateById(updateArticle);
        if(i != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "修改文章失败");
        }

        //如果用户是草稿，现在修改为发布，则进行文章权益调整，发布到好友动态
        if(updateArticle.getIsRelease() != null && updateArticle.getIsRelease() && !article.getIsRelease()){
            articleRightService.updateArticleRight(userId);
            sendFrientFeed(updateArticle , userId);
        }
    }

    //用户删除文章,如果是江湖文章是真删除，如果是专栏文章同步到江湖的，将会改为专栏文章，不同步到江湖
    /*
    1：如果是在江湖发布的文章，那么直接删除
    2：如果是在专栏发布的文章同步到了江湖，则将其改为专栏内的文章，未同步到江湖
     */
    @Override
    public void deleteArticle(String articleId, String userId) {
        //查询文章
        Article article = baseMapper.selectById(articleId);
        if(article == null){
            throw new XiaoXiaException(ResultCode.ERROR , "该文章不存在");
        }

        //校验修改者是否和文章所有者相同
        if(!userId.equals(article.getUserId())){
            throw new XiaoXiaException(ResultCode.ERROR , "请不要非法修改文章");
        }

        //专栏文章，未同步到江湖
        if(article.getIsColumnArticle() && !article.getIsBbs()){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确删除");
        }

        //下面有两种状况
        //专栏文章，同步到江湖
        if(article.getIsColumnArticle() && article.getIsBbs()){
            Article articleUpdate = new Article();
            articleUpdate.setId(articleId);
            articleUpdate.setIsBbs(false);
            int i = baseMapper.updateById(articleUpdate);
            if(i != 1){
                throw new XiaoXiaException(ResultCode.ERROR , "删除失败");
            }
        }else {
            //江湖文章
            int i = baseMapper.deleteById(articleId);
            if(i != 1){
                throw new XiaoXiaException(ResultCode.ERROR , "删除失败");
            }
            labelService.deleteArticleLabel(articleId);
        }
        commentService.deleteCommentByArticleId(articleId);
    }

    //查询置顶文章
    @Cacheable(value = "TopArticle")
    @Override
    public IndexArticleVo findTopArticle() {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("is_top" , 1);
        Article article = baseMapper.selectOne(wrapper);
        IndexArticleVo indexArticleVo = new IndexArticleVo();
        BeanUtils.copyProperties(article , indexArticleVo);

        Category categoryByCategoryId = categoryService.findCategoryByCategoryId(article.getCategoryId());
        indexArticleVo.setCategoryName(categoryByCategoryId.getCategoryName());

        return indexArticleVo;
    }



    //向好友动态发送消息
    @Async
    @Override
    public void sendFrientFeed(Article article, String userId) {
        R userFansId = ucenterClient.findUserFansId(userId);
        if(!userFansId.getSuccess()){
            return;
        }
        List<String> userIdList = (List<String>) userFansId.getData().get("userIdList");
        //获取分类名称
        Category categoryByCategoryId = categoryService.findCategoryByCategoryId(article.getCategoryId());
        String categoryName = categoryByCategoryId.getCategoryName();
        //向好友动态发送消息
        InfoFriendFeedVo infoFriendFeedVo = new InfoFriendFeedVo(article.getId() , article.getTitle() ,
                article.getDescription() , article.getUserId() , article.getNickname() , article.getAvatar() , categoryName  , userIdList);
        msgProducer.sendFriendFeed(JSON.toJSONString(infoFriendFeedVo));
    }

    //设置文章访问量，缓存处理
    @Override
    public void setArticleViews(String articleId, String ip) {
        //设置以文章为key的set
        RedisUtils.setSet(articleId , ip);
        //设置以文章为key的键的存活时间
        RedisUtils.expire(articleId , RedisUtils.ARTICLEVIEWTIME , TimeUnit.MINUTES);
        //将缓存当前访问过哪些文章
        RedisUtils.setSet(RedisUtils.ARTICLLE , articleId);
        //设置存活时间
        RedisUtils.expire(RedisUtils.ARTICLLE , RedisUtils.ARTICLEVIEWTIME , TimeUnit.MINUTES);
    }


    //为消息模块服务，查询文章浏览量
    @Override
    public Map<String, Object> findArticleViews(List<String> articleIdList) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.select("id" , "views");
        wrapper.in("id" , articleIdList);
        List<Article> articleList = baseMapper.selectList(wrapper);
        Map<String , Object> views = new HashMap<>();
        for(Article article : articleList){
            views.put(article.getId() , article.getViews());
        }
        return views;
    }

    //为用户模块服务，查询用户文章在江湖可以查找到的文章
    @Override
    public Integer findUserbbsArticleNumber(String userId) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.eq("is_violation_article" , 0);
        wrapper.last("and (is_release = 1 or is_bbs = 1)");
        return baseMapper.selectCount(wrapper);
    }

    //查询出文章浏览量
    @Override
    public List<Article> findArticleViewsList(List<String> articleIdList) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.select("id" , "views");
        wrapper.in("id" , articleIdList);
        return baseMapper.selectList(wrapper);
    }

    //更新文章浏览量
    @Override
    public void updateArticleViews(List<Article> articleUpdateList) {
        baseMapper.updateArticleViews(articleUpdateList);
    }

    //查询用户所有江湖文章数量
    @Override
    public Integer findUserAllArticleNumber(String userId) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.last("and (is_column_article = 0 or is_bbs = 1)");
        return baseMapper.selectCount(wrapper);
    }

    //查找用户在江湖的所有文章
    @Override
    public List<UserArticleVo> findUserAllArticle(String userId, Long current, Long limit) {

        //进行分页
        current = (current - 1) * limit;

        //进行查询用户在江湖的文章
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.last("and (is_column_article = 0 or is_bbs = 1) order by gmt_create desc limit " + current + " , " + limit);
        List<Article> articleList = baseMapper.selectList(wrapper);

        //进行数据的封装
        List<UserArticleVo> userArticleVoList = new ArrayList<>();
        for(Article article : articleList){
            UserArticleVo userArticleVo = new UserArticleVo();
            BeanUtils.copyProperties(article , userArticleVo);
            userArticleVo.setArticleId(article.getId());


            Category category = categoryService.findCategoryByCategoryId(article.getCategoryId());
            userArticleVo.setCategoryName(category.getCategoryName());


            if(article.getIsColumnArticle() && article.getIsBbs()){
                userArticleVo.setIsRelease(true);
            }
            List<String> articleLabel = labelService.findArticleLabel(article.getId());
            userArticleVo.setLabelList(articleLabel);
            userArticleVoList.add(userArticleVo);
        }
        return userArticleVoList;
    }

    //查询他人在江湖发布的文章
    @Override
    public List<OtherUserArticleVo> findOtherUserArticle(String userId, Long current, Long limit) {
        current = (current - 1) * limit;
        return baseMapper.findOtherUserArticle(userId , current , limit);
    }

    //修改专栏文章的排序
    @Override
    public void updateArticleSort(String articleId, String userId, Long sort) {
        //这里不用检查是否为专栏还是江湖，这个字段数据不正确也没问题
        Article article = new Article();
        article.setSort(sort);
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("id" , articleId);
        wrapper.eq("user_id" , userId);
        int update = baseMapper.update(article, wrapper);
        if(update != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "修改失败");
        }
    }


    //查询专栏文章
    @Override
    public ColumnArticleDetailVo findColunmArticle(String articleId) {
        Article article = baseMapper.selectById(articleId);
        ColumnArticleDetailVo columnArticleDetailVo = new ColumnArticleDetailVo();
        BeanUtils.copyProperties(article , columnArticleDetailVo);
        columnArticleDetailVo.setArticleId(articleId);
        Category categoryByCategoryId = categoryService.findCategoryByCategoryId(article.getCategoryId());
        columnArticleDetailVo.setCategoryName(categoryByCategoryId.getCategoryName());
        return columnArticleDetailVo;
    }

}
