package com.kuang.message.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SystemVo {
    private String id;
    private String title;
    private String content;
    private Date gmtCreate;
}
