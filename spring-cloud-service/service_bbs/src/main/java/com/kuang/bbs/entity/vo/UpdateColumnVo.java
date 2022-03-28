package com.kuang.bbs.entity.vo;

import lombok.Data;

@Data
public class UpdateColumnVo {

    private String title;
    private String description;
    private String color;
    private Long vsibility;
    private Boolean isRelease;
}
