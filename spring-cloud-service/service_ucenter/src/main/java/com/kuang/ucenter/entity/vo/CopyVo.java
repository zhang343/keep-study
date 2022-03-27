package com.kuang.ucenter.entity.vo;


import lombok.Data;

@Data
public class CopyVo {
    //vip等级
    private String vipLevel;
    private Integer attentionNumber;
    private Integer fansNumber;
    //已经发布到江湖还可以看到的文章
    private Integer bbsArticleNumber;
    private Integer studyNumber;
    //所有的评论数量
    private Integer commentNumber;
}
