package com.kuang.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.message.entity.InfoMyNews;
import com.kuang.message.entity.vo.MyNewsVo;
import com.kuang.message.mapper.InfoMyNewsMapper;
import com.kuang.message.service.InfoMyNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@Service
@Slf4j
public class InfoMyNewsServiceImpl extends ServiceImpl<InfoMyNewsMapper, InfoMyNews> implements InfoMyNewsService {

    //查找未读消息
    @Override
    public Integer findUserUnreadNumber(String userId) {
        QueryWrapper<InfoMyNews> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.eq("is_read" , 0);
        return baseMapper.selectCount(wrapper);
    }

    //查询用户我的消息数量
    @Override
    public Integer findUserNewsNumber(String userId) {
        log.info("查询用户我的消息数量");
        QueryWrapper<InfoMyNews> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }

    //查询用户我的消息
    @Override
    public List<MyNewsVo> findUserNews(Long current, Long limit, String userId) {
        log.info("查询用户我的消息");
        current = (current - 1) * limit;
        return baseMapper.findUserNews(current , limit , userId);
    }

    //让用户我的消息已读
    @Async
    @Override
    public void setMyNewsRead(List<MyNewsVo> myNewsVos) {
        log.info("让用户我的消息已读");
        List<String> idList = new ArrayList<>();
        for(MyNewsVo myNewsVo : myNewsVos){
            idList.add(myNewsVo.getId());
        }
        if(idList.size() != 0){
            baseMapper.setMyNewsRead(idList);
        }
    }
}
