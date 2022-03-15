package com.kuang.springcloud.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@Data
public class UserStudyVo implements Serializable {


    private String userId;
    private String courseId;
    private String title;
    private String cover;
}
