package com.kuang.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.bbs.entity.Article;
import com.kuang.bbs.entity.Category;
import com.kuang.bbs.entity.vo.IndexArticleVo;
import com.kuang.bbs.es.entity.EsArticle;
import com.kuang.bbs.mapper.ArticleMapper;
import com.kuang.bbs.service.CategoryService;
import com.kuang.bbs.service.ESArticleSearchService;
import com.kuang.springcloud.utils.RedisUtils;
import com.kuang.springcloud.utils.VipUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ESArticleSearchServiceImpl implements ESArticleSearchService {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private CategoryService categoryService;

    @Override
    public Map<String, Object> pageArticleCondition(Long current, Long limit, String categoryId, Boolean isExcellentArticle, String articleNameOrLabelName) {
        current = (current - 1) * limit;

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //分类查询
        if(!StringUtils.isEmpty(categoryId)){
            boolQueryBuilder.must(QueryBuilders.termQuery("categoryId" , categoryId));
        }

        //精品文章查询
        if(isExcellentArticle != null && isExcellentArticle){
            boolQueryBuilder.must(QueryBuilders.termQuery("isExcellentArticle" , true));
        }



        //搜索字段的查询
        if(!StringUtils.isEmpty(articleNameOrLabelName)){
            QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(articleNameOrLabelName , "title" , "labelList" , "description");
            boolQueryBuilder.must(queryBuilder);
        }

        //要求文章为已经发布到江湖的
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(true , "isRelease" , "isBbs");
        boolQueryBuilder.must(queryBuilder);


        //去除封禁文章
        boolQueryBuilder.mustNot(QueryBuilders.termQuery("isViolationArticle" , true));

        HighlightBuilder.Field  highlightFieldDescription = new HighlightBuilder.Field("description")
                .preTags("<span style='color:red'>")
                .postTags("</span>");

        HighlightBuilder.Field  highlightFieldTitle = new HighlightBuilder.Field("title")
                .preTags("<span style='color:red'>")
                .postTags("</span>");

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                //分页
                .withPageable(PageRequest.of(Math.toIntExact(current), Math.toIntExact(limit)))
                //这里只查询id
                .withFields("id" , "title" , "description")
                //高亮
                .withHighlightFields(highlightFieldTitle , highlightFieldDescription)
                .build();

        //查询数据量
        long articleNumber = elasticsearchRestTemplate.count(nativeSearchQuery, EsArticle.class, IndexCoordinates.of("article"));
        List<EsArticle> esArticleList = new ArrayList<>();
        //查询数据
        SearchHits<EsArticle> article = elasticsearchRestTemplate.search(nativeSearchQuery, EsArticle.class, IndexCoordinates.of("article"));
        for(SearchHit<EsArticle> searchHits : article){
            System.out.println(searchHits.getContent().getId() + "=======" +searchHits.getScore() + "========" +searchHits.getContent());
            //高亮字段的替换
            EsArticle content = searchHits.getContent();
            Map<String, List<String>> highlightFields = searchHits.getHighlightFields();
            for (Map.Entry<String, List<String>> stringHighlightFieldEntry : highlightFields.entrySet()) {
                String key = stringHighlightFieldEntry.getKey();
                if ("title".equals(key)) {
                    List<String> fragments = stringHighlightFieldEntry.getValue();
                    StringBuilder sb = new StringBuilder();
                    for (String fragment : fragments) {
                        sb.append(fragment);
                    }
                    content.setTitle(sb.toString());
                }
                if ("description".equals(key)) {
                    List<String> fragments = stringHighlightFieldEntry.getValue();
                    StringBuilder sb = new StringBuilder();
                    for (String fragment : fragments) {
                        sb.append(fragment);
                    }
                    content.setDescription(sb.toString());
                }
            }
            esArticleList.add(content);
        }

        //如果数据为空，直接返回
        if(esArticleList.size() == 0){
            Map<String , Object> map = new HashMap<>();
            map.put("total" , 0);
            map.put("articleList" , new ArrayList<IndexArticleVo>());
            return map;
        }



        List<IndexArticleVo> indexArticleVoList = new ArrayList<>();
        //从数据库中取出数据
        for(EsArticle esArticle : esArticleList){
            IndexArticleVo indexArticleVo = new IndexArticleVo();

            //处理基本数据
            QueryWrapper<Article> wrapper = new QueryWrapper<>();
            wrapper.select("id" , "is_top" , "user_id" , "nickname" , "avatar" , "views" , "category_id" , "is_excellent_article" , "gmt_create");
            wrapper.eq("id" , esArticle.getId());
            Article article1 = articleMapper.selectOne(wrapper);
            BeanUtils.copyProperties(article1 , indexArticleVo);
            indexArticleVo.setTitle(esArticle.getTitle());
            indexArticleVo.setDescription(esArticle.getDescription());


            //处理浏览量
            long setSize = RedisUtils.getSetSize(indexArticleVo.getId());
            indexArticleVo.setViews(setSize + indexArticleVo.getViews());

            //处理分类
            Category category = categoryService.findCategoryByCategoryId(article1.getCategoryId());
            indexArticleVo.setCategoryName(category.getCategoryName());


            indexArticleVoList.add(indexArticleVo);
        }

        //处理vip
        VipUtils.setVipLevel(indexArticleVoList , indexArticleVoList.get(0));

        //返回数据
        Map<String , Object> map = new HashMap<>();
        map.put("total" , articleNumber);
        map.put("articleList" , indexArticleVoList);
        return map;
    }
}
