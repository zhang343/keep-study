package com.kuang.bbs.es.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "article", shards = 3, replicas = 1)
public class EsArticle{
    //必须有 id,这里的 id 是全局唯一的标识，等同于 es 中的"_id"
    @Id
    private String id;//文章id唯一标识
    /**
     * type : 字段数据类型
     * analyzer : 分词器类型
     * index : 是否索引(默认:true)
     * Keyword : 短语,不进行分词
     */
    @Field(type = FieldType.Keyword , index = true)
    private String categoryId;
    @Field(type = FieldType.Text , analyzer = "ik_max_word" , index = true  , searchAnalyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text , analyzer = "ik_max_word" , index = true  , searchAnalyzer = "ik_max_word")
    private String description;


    @Field(type = FieldType.Boolean ,  index = true)
    private Boolean isColumnArticle;
    @Field(type = FieldType.Boolean ,  index = true)
    private Boolean isRelease;
    @Field(type = FieldType.Boolean ,  index = true)
    private Boolean isBbs;
    @Field(type = FieldType.Boolean ,  index = true)
    private Boolean isViolationArticle;


    @Field(type = FieldType.Boolean ,  index = true)
    private Boolean isExcellentArticle;
    @Field(type = FieldType.Text , analyzer = "ik_max_word" , index = true , searchAnalyzer = "ik_max_word")
    private String labelList;
}
