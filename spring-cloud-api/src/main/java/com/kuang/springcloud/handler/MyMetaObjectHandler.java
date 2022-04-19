package com.kuang.springcloud.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;



//处理数据自动填充
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("gmtCreate" , new Date() , metaObject);
        this.setFieldValByName("gmtModified" , new Date() , metaObject);
    }


    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified" , new Date() , metaObject);
    }
}
