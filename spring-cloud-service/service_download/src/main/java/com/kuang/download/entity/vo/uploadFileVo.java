package com.kuang.download.entity.vo;

import lombok.Data;

@Data
public class uploadFileVo {

    private String categoryId;
    private String name;
    private String size;
    private Integer price;
    private String fileSourceId;
}
