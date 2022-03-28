package com.kuang.bbs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.bbs.client.UcenterClient;
import com.kuang.bbs.client.VipClient;
import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.ArticleRight;
import com.kuang.bbs.entity.Category;
import com.kuang.bbs.entity.vo.*;
import com.kuang.bbs.mapper.ArticleMapper;
import com.kuang.bbs.mapper.ArticleRightMapper;
import com.kuang.bbs.service.ArticleService;
import com.kuang.bbs.service.CategoryService;
import com.kuang.bbs.service.CommentService;
import com.kuang.bbs.service.LabelService;
import com.kuang.springcloud.entity.InfoFriendFeedVo;
import com.kuang.springcloud.entity.RightRedis;
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
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private VipClient vipClient;

    @Resource
    private UcenterClient ucenterClient;

    @Resource
    private MsgProducer msgProducer;

    @Resource
    private CategoryService categoryService;

    @Resource
    private ArticleRightMapper articleRightMapper;

    @Resource
    private CommentService commentService;

    @Resource
    private LabelService labelService;


    //查询系统文章数量
    @Cacheable(value = "articleNumber")
    @Override
    public Integer findArticleNumber() {
        log.info("查询系统文章数量");
        return baseMapper.selectCount(null);
    }

    //查询文章详细数据
    @Override
    public ArticleVo findArticleDetail(String articleId , String userId) {
        log.info("查询文章详细数据,文章id:" + articleId);
        Article article = baseMapper.selectById(articleId);
        if(article == null){
            log.warn("用户非法查询不存在文章,文章id:" + articleId);
            throw new XiaoXiaException(ResultCode.ERROR , "非法查询不存在文章");
        }

        //下面进行数据验证
        String articleUserId = article.getUserId();
        //如果是专栏文章，但是没有同步到江湖
        if(article.getIsColumnArticle() && !article.getIsBbs()){
            throw new XiaoXiaException(ResultCode.ERROR , "非法查询不存在文章");
        }
        //如果查询文章的和文章所有者不一致
        if(!articleUserId.equals(userId)){
            //查询文章的和文章所有者不一致
            //违规不可查
            if(article.getIsViolationArticle()){
                throw new XiaoXiaException(ResultCode.ERROR , "非法查询不存在文章");
            }
            //非专栏文章,但是没发布
            if(!article.getIsColumnArticle() && !article.getIsRelease()){
                throw new XiaoXiaException(ResultCode.ERROR , "非法查询不存在文章");
            }
        }

        ArticleVo articleVo = new ArticleVo();
        //设置分类
        Category categoryByCategoryId = categoryService.findCategoryByCategoryId(article.getCategoryId());
        if(categoryByCategoryId != null){
            articleVo.setCategoryName(categoryByCategoryId.getCategoryName());
        }
        BeanUtils.copyProperties(article , articleVo);
        if(article.getIsColumnArticle() && article.getIsBbs()){
            articleVo.setIsRelease(true);
        }
        String userVipLevel = VipUtils.getUserVipLevel(userId);
        articleVo.setVipLevel(userVipLevel);
        return articleVo;
    }

    //文章缓存,时间是30分钟
    @Async
    @Override
    public void setArticleCache(ArticleCacheVo articleCacheVo, String userId) {
        log.info("设置文章的缓存,用户id:" + userId);
        if(articleCacheVo.getLabelList() != null && articleCacheVo.getLabelList().size() != 0){
            Set<String> labelSet = new HashSet<>(articleCacheVo.getLabelList());
            List<String> labelList = new ArrayList<>(labelSet);
            articleCacheVo.setLabelList(labelList);
        }
        RedisUtils.setValueTimeout(userId , articleCacheVo , 30 * 60);
    }

    //查询文章缓存
    @Override
    public ArticleCacheVo findArticleCache(String userId) {
        log.info("查询文章的缓存,用户id:" + userId);
        Object value = RedisUtils.getValue(userId);
        if(value == null){
            return null;
        }
        return (ArticleCacheVo) value;
    }

    //根据条件查询系统文章总数
    @Override
    public Long findArticleNumber(String categoryId, Boolean isExcellentArticle, String articleNameOrLabelName) {
        log.info("根据条件查询系统文章总数");
        return baseMapper.findArticleNumber(categoryId , isExcellentArticle , articleNameOrLabelName);
    }

    //条件分页查询文章
    @Async
    @Override
    public Future<List<IndexArticleVo>> pageArticleCondition(Long current, Long limit, String categoryId, Boolean isExcellentArticle, String articleNameOrLabelName) {
        log.info("条件分页查询文章");
        List<IndexArticleVo> indexArticleVoList = baseMapper.pageArticleCondition((current - 1) * limit , limit , categoryId , isExcellentArticle , articleNameOrLabelName);
        if(current == 1 && limit == 10 && StringUtils.isEmpty(categoryId) && StringUtils.isEmpty(articleNameOrLabelName) && (isExcellentArticle == null || !isExcellentArticle)){
            //看redis是否有缓存
            Object o = RedisUtils.getValue("TopArticleList");
            if(o != null){
                indexArticleVoList = (List<IndexArticleVo>) o;
            }else {
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
                RedisUtils.setValueTimeout("TopArticleList" , indexArticleVoList , 30);
            }
        }
        VipUtils.setVipLevel(indexArticleVoList , indexArticleVoList.get(0));
        return new AsyncResult<>(indexArticleVoList);
    }

    //用户发布文章
    @Transactional
    @Override
    public Article addArticle(ArticleUpdateAndCreateVo articleUpdateAndCreateVo , String avatar , String nickname  , String userId) {
        log.info("用户发布文章,用户id:" + userId);

        RightRedis userRightRedis = VipUtils.getUserRightRedis(userId);
        if(userRightRedis == null){
            R rightRedisByUserId = vipClient.findRightRedisByUserId(userId);
            if(!rightRedisByUserId.getSuccess()){
                throw new XiaoXiaException(ResultCode.ERROR , "发布文章失败");
            }
            userRightRedis = (RightRedis) rightRedisByUserId.getData().get("rightRedis");
        }

        Article article = new Article();
        BeanUtils.copyProperties(articleUpdateAndCreateVo , article);
        article.setId(null);
        article.setUserId(userId);
        article.setAvatar(avatar);
        article.setNickname(nickname);
        int insert = baseMapper.insert(article);
        if(insert != 1){
            log.warn("增加文章失败,用户id:" + userId);
            throw new XiaoXiaException(ResultCode.ERROR , "发布文章失败");
        }


        QueryWrapper<ArticleRight> wrapper = new QueryWrapper<>();
        wrapper.select("id" , "money" , "article_number" , "version");
        wrapper.eq("user_id" , userId);
        ArticleRight articleRight = articleRightMapper.selectOne(wrapper);
        if(articleRight.getArticleNumber() + 1 > userRightRedis.getArticleNumber()){
            throw new XiaoXiaException(ResultCode.ERROR , "今日文章发布数量已满");
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
        return article;
    }

    //查询文章通过文章id和用户id
    @Override
    public ArticleUpdateAndCreateVo findArticleByArticleIdAndUserId(String articleId, String userId) {
        log.info("文章数据的查询,配合文章数据的修改,用户id:" + userId + ",文章id:" + articleId);
        Article article = baseMapper.selectById(articleId);
        if(article == null){
            throw new XiaoXiaException(ResultCode.ERROR , "没有该文章");
        }
        if(!userId.equals(article.getUserId())){
            throw new XiaoXiaException(ResultCode.ERROR , "非法修改不不属于的文章");
        }
        //对于文章所有者和文章查询者相同，我们对于江湖文章不用判断，但是如果是专栏文章，要判断
        //专栏文章，但是没有同步到江湖
        if(article.getIsColumnArticle() && !article.getIsBbs()){
            throw new XiaoXiaException(ResultCode.ERROR , "非法查询不存在文章");
        }
        ArticleUpdateAndCreateVo articleUpdateAndCreateVo = new ArticleUpdateAndCreateVo();
        BeanUtils.copyProperties(article , articleUpdateAndCreateVo);
        if(article.getIsColumnArticle() && article.getIsBbs()){
            articleUpdateAndCreateVo.setIsRelease(true);
        }
        return articleUpdateAndCreateVo;
    }

    //用户修改文章
    @Override
    public void updateArticle(ArticleUpdateAndCreateVo articleUpdateAndCreateVo , String userId) {
        String articleId = articleUpdateAndCreateVo.getId();
        log.info("用户修改文章,用户id:" + userId + ",文章id:" + articleId);
        Article article = baseMapper.selectById(articleId);
        if(article == null){
            throw new XiaoXiaException(ResultCode.ERROR , "非法修改不存在文章");
        }
        if(!userId.equals(article.getUserId())){
            throw new XiaoXiaException(ResultCode.ERROR , "非法修改不不属于的文章");
        }
        //专栏文章，但是没有同步到江湖
        if(article.getIsColumnArticle() && !article.getIsBbs()){
            throw new XiaoXiaException(ResultCode.ERROR , "非法查询不存在文章");
        }


        if(article.getIsColumnArticle() && article.getIsBbs()){
            articleUpdateAndCreateVo.setIsRelease(null);
        }else if(!article.getIsColumnArticle() && article.getIsRelease()){
            articleUpdateAndCreateVo.setIsRelease(null);
        }

        Article article1 = new Article();
        BeanUtils.copyProperties(articleUpdateAndCreateVo , article1);
        //这里不用乐观锁,因为这里修改的字段只有用户本人可用修改
        int i = baseMapper.updateById(article1);
        if(i != 1){
            log.warn("修改文章失败,用户id:" + userId + ",文章id:" + article.getId());
            throw new XiaoXiaException(ResultCode.ERROR , "修改文章失败");
        }

        if(articleUpdateAndCreateVo.getIsRelease() == null || !articleUpdateAndCreateVo.getIsRelease()){
            return;
        }

        if(!article.getIsColumnArticle() && !article.getIsRelease() && articleUpdateAndCreateVo.getIsRelease()){
            sendFrientFeed(articleId , userId);
        }
    }

    //用户删除文章
    /*
    1：如果是在江湖发布的文章，那么直接删除
    2：如果是在专栏发布的文章同步到了江湖，则将其改为专栏内的文章，未同步到江湖
     */
    @Override
    public void deleteArticle(String articleId, String userId) {
        log.info("用户删除文章,用户id:" + userId + ",文章id:" + articleId);
        Article article = baseMapper.selectById(articleId);
        if(article == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确删除");
        }
        if(!userId.equals(article.getUserId())){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确删除");
        }
        if(article.getIsColumnArticle() && !article.getIsBbs()){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确删除");
        }

        //下面有两种状况
        if(article.getIsColumnArticle() && article.getIsBbs()){
            //专栏文章同步到江湖的状态
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
        return baseMapper.findTopArticle();
    }



    //向好友动态发送消息
    @Async
    @Override
    public void sendFrientFeed(String articleId, String userId) {
        Article article = baseMapper.selectById(articleId);
        if(article == null){
            return;
        }
        R userFansId = ucenterClient.findUserFansId(userId);
        if(!userFansId.getSuccess()){
            return;
        }
        List<String> userIdList = (List<String>) userFansId.getData().get("userIdList");
        //获取分类名称
        Category categoryByCategoryId = categoryService.findCategoryByCategoryId(article.getCategoryId());
        String categoryName = null;
        if(categoryByCategoryId != null){
            categoryName = categoryByCategoryId.getCategoryName();
        }
        //向好友动态发送消息
        try {
            log.warn("发送消息到好友动态,文章id:" + article.getId());
            //发送
            InfoFriendFeedVo infoFriendFeedVo = new InfoFriendFeedVo(article.getId() , article.getTitle() ,
                    article.getDescription() , article.getUserId() , article.getNickname() , article.getAvatar() , categoryName  , userIdList);
            msgProducer.sendFriendFeed(JSON.toJSONString(infoFriendFeedVo));
        }catch(Exception e){
            log.warn("发送消息到好友动态失败,文章id:" + article.getId());
        }
    }

    //设置文章访问量，缓存处理
    @Async
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
        List<Article> articleList = baseMapper.selectBatchIds(articleIdList);
        Map<String , Object> views = new HashMap<>();
        for(Article article : articleList){
            long value = RedisUtils.getSetSize(article.getId());
            long view = article.getViews() + value;
            views.put(article.getId() , view);
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
        return baseMapper.findArticleViewsList(articleIdList);
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
    public List<UserArticleVo> findUserArticle(String userId, Long current, Long limit) {
        current = (current - 1) * limit;
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.last("and (is_column_article = 0 or is_bbs = 1) order by gmt_create desc limit " + current + " , " + limit);
        List<Article> articleList = baseMapper.selectList(wrapper);
        List<UserArticleVo> userArticleVoList = new ArrayList<>();
        TreeMap<String, Category> allCategoryTreeMap = categoryService.findAllCategoryTreeMap();
        for(Article article : articleList){
            UserArticleVo userArticleVo = new UserArticleVo();
            BeanUtils.copyProperties(article , userArticleVo);
            userArticleVo.setArticleId(article.getId());
            Category category = allCategoryTreeMap.get(article.getCategoryId());
            if(category != null){
                userArticleVo.setCategoryName(category.getCategoryName());
            }
            if(article.getIsColumnArticle() && article.getIsBbs()){
                userArticleVo.setIsRelease(true);
            }
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

}
