package com.kuang.springcloud.entity;

import lombok.Data;

@Data
public class RightRedis implements Comparable<RightRedis>{
    private String id;
    private String vipLevel;
    private Integer price;
    private Double courseDiscount;
    private Integer columnNumber;
    private Integer signExperience;
    private Integer money;
    private Integer articleNumber;
    private Integer timeLength;

    @Override
    public int compareTo(RightRedis o) {
        if(price > (int) o.getPrice()){
            return 1;
        }

        if(price < (int) o.getPrice()){
            return -1;
        }

        return 0;
    }
}
