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
import com.kuang.bbs.service.ArticleService;
import com.kuang.bbs.service.CategoryService;
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


    //查询系统文章数量
    @Cacheable(value = "articleNumber")
    @Override
    public Integer findArticleNumber() {
        log.info("查询系统文章数量");
        return baseMapper.selectCount(null);
    }

    //查询文章详细数据
    @Async
    @Override
    public Future<ArticleVo> findArticleDetail(String articleId , String userId) {
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
        String userVipLevel = VipUtils.getUserVipLevel(userId);
        if(userVipLevel == null){
            R vipR = vipClient.findRightRedisByUserId(articleUserId);
            if(vipR.getSuccess()){
                RightRedis rightRedis = (RightRedis) vipR.getData().get("rightRedis");
                userVipLevel = rightRedis.getVipLevel();
            }
        }
        articleVo.setVipLevel(userVipLevel);
        Object value = RedisUtils.getValue(articleId);
        if(value != null){
            Integer views = (Integer) value;
            articleVo.setViews(articleVo.getViews() + views);
        }
        return new AsyncResult<>(articleVo);
    }

    //文章缓存,时间是30分钟
    @Async
    @Override
    public void setArticleCache(ArticleCacheVo articleCacheVo, String userId) {
        log.info("设置文章的缓存,用户id:" + userId);
        Set<String> labelSet = new HashSet<>(articleCacheVo.getLabelList());
        List<String> labelList = new ArrayList<>(labelSet);
        articleCacheVo.setLabelList(labelList);
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
            }
            RedisUtils.setValueTimeout("TopArticleList" , indexArticleVoList , 30);
        }

        List<String> userIdList = new ArrayList<>();
        for(IndexArticleVo indexArticleVo : indexArticleVoList){
            Object value = RedisUtils.getValue(indexArticleVo.getId());
            if(value != null){
                Integer views = (Integer) value;
                indexArticleVo.setViews(indexArticleVo.getViews() + views);
            }
            userIdList.add(indexArticleVo.getUserId());
        }


        Map<String , String> userVipLevel = VipUtils.getUserVipLevel(userIdList);
        if(userVipLevel != null){
            for(IndexArticleVo indexArticleVo : indexArticleVoList){
                String vipLevel = userVipLevel.get(indexArticleVo.getUserId());
                indexArticleVo.setVipLevel(vipLevel);
            }
        }
        return new AsyncResult<>(indexArticleVoList);
    }

    //用户发布文章
    @Transactional
    @Override
    public Article addArticle(ArticleUpdateAndCreateVo articleUpdateAndCreateVo , String userId) {
        log.info("用户发布文章,用户id:" + userId);
        Article article = new Article();
        BeanUtils.copyProperties(articleUpdateAndCreateVo , article);
        article.setId(null);
        article.setUserId(userId);
        //进行远程调用查找发布文章的用户头像和昵称
        R ucenterR = ucenterClient.findAvatarAndNicknameByUserId(userId);
        if(!ucenterR.getSuccess()){
            log.warn("远程调用失败，无法查询出用户头像和昵称");
            throw new XiaoXiaException(ResultCode.ERROR , "发布文章失败");
        }
        String avatar = (String) ucenterR.getData().get("avatar");
        String nickname = (String) ucenterR.getData().get("nickname");
        article.setAvatar(avatar);
        article.setNickname(nickname);
        int insert = baseMapper.insert(article);
        if(insert != 1){
            log.warn("增加文章失败,用户id:" + userId);
            throw new XiaoXiaException(ResultCode.ERROR , "发布文章失败");
        }

        //进行远程调用调整用户每日权益
        R vipR = vipClient.addArticle(userId);
        if(!vipR.getSuccess()){
            log.warn("远程调用失败,无法调整用户每日权益");
            throw new XiaoXiaException(ResultCode.ERROR , "发布文章失败");
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
        if(article.getIsRelease() || article.getIsBbs()){
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

        if(!article.getIsColumnArticle() && !article.getIsRelease() && articleUpdateAndCreateVo.getIsRelease()){
            sendFrientFeed(articleId , userId);
        }
    }

    //用户删除文章
    @Override
    public void deleteArticle(String articleId, String userId) {
        log.info("用户删除文章,用户id:" + userId + ",文章id:" + articleId);
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("id" , articleId);
        wrapper.eq("user_id" , userId);
        int delete = baseMapper.delete(wrapper);
        if(delete != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "用户删除文章失败");
        }
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


    //更新文章浏览量
    @Override
    public void updateArticleViews(List<Article> articleList) {
        baseMapper.updateArticleViews(articleList);
    }

    //为消息模块服务，查询文章浏览量
    @Override
    public Map<String, Object> findArticleViews(List<String> articleIdList) {
        List<Article> articleList = baseMapper.selectBatchIds(articleIdList);
        Map<String , Object> views = new HashMap<>();
        for(Article article : articleList){
            Object value = RedisUtils.getValue(article.getId());
            Long view = article.getViews();
            if(value != null){
                view = view + (Long) value;
            }
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

}
