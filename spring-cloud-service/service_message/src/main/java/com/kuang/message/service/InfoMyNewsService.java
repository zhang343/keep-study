package com.kuang.message.service;

import com.kuang.message.entity.InfoMyNews;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.message.entity.vo.MyNewsVo;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
public interface InfoMyNewsService extends IService<InfoMyNews> {


    //查找未读消息
    Integer findUserUnreadNumber(String userId);

    //查询用户我的消息数量
    Integer findUserNewsNumber(String userId);

    //查询用户我的消息
    List<MyNewsVo> findUserNews(Long current, Long limit, String userId);

    //让用户我的消息已读
    void setMyNewsRead(List<MyNewsVo> myNewsVos);
}
