package com.kuang.bbs.es.mapper;




import com.kuang.bbs.es.entity.EsArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsArticleMapper extends ElasticsearchRepository<EsArticle,String> {
}
