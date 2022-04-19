package com.kuang.message.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuang.message.entity.InfoSystem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.message.entity.vo.SystemVo;

import java.util.List;


public interface InfoSystemService extends IService<InfoSystem> {

    //查询未读消息
    Integer findUserUnreadNumber(String userId);

    //查看用户消息数量
    Integer findUserNewsNumber(String userId);

    //查看用户消息
    List<SystemVo> findUserNews(Long current, Long limit, String userId);

    //让系统消息已读
    void setSystemNewsRead(List<SystemVo> systemVos);
}
