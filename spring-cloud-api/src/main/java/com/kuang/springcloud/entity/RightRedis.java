package com.kuang.springcloud.entity;

import lombok.Data;

@Data
public class RightRedis {
    private String id;
    private String vipLevel;
    private Integer price;
    private Double courseDiscount;
    private Integer columnNumber;
    private Integer signExperience;
    private Integer money;
    private Integer articleNumber;
    private Integer timeLength;
}
