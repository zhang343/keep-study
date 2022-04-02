package com.kuang.ucenter.entity.vo;


import lombok.Data;

@Data
public class CopyVo {
    //vip等级
    private String vipLevel;
    //已经发布到江湖还可以看到的文章
    private Integer bbsArticleNumber;
    private Integer studyNumber;
}
