package com.kuang.course.entity.vo;

import cn.hutool.core.date.DateUtil;
import lombok.Data;

@Data
public class IndexCourseVo {

    //课程id
    private String id;
    //课程标题
    private String title;
    //课程描述
    private String description;
    //课程封面
    private String cover;
    //课程时长，这里是
    private String totalLength;
    //课程状态
    private String status;
    //课程播放量
    private Long views;

    public void setTotalLength(Object totalLength) {
        if(totalLength instanceof String){
            //当我们对这个对象缓存到redis里面，反序列化的时候这个字段是字符串，所以直接赋值
            this.totalLength = (String) totalLength;
        }else {
            //当我们从数据库查询的时候，这个字段是long类型，我们需要转换成hh:mm:ss
            long time = (long) totalLength;
            this.totalLength = DateUtil.secondToTime(Math.toIntExact(time));
        }
    }
}
