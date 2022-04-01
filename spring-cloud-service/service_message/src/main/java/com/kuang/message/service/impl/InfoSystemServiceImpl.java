package com.kuang.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuang.message.entity.InfoCourse;
import com.kuang.message.entity.InfoSystem;
import com.kuang.message.entity.vo.SystemVo;
import com.kuang.message.mapper.InfoSystemMapper;
import com.kuang.message.service.InfoSystemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class InfoSystemServiceImpl extends ServiceImpl<InfoSystemMapper, InfoSystem> implements InfoSystemService {

    //查询未读消息
    @Override
    public Integer findUserUnreadNumber(String userId) {
        QueryWrapper<InfoSystem> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.eq("is_read" , 0);
        return baseMapper.selectCount(wrapper);
    }

    //查看用户消息数量
    @Override
    public Integer findUserNewsNumber(String userId) {
        log.info("查看用户消息数量");
        QueryWrapper<InfoSystem> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }

    //查看用户消息
    @Override
    public List<SystemVo> findUserNews(Long current, Long limit, String userId) {
        current = (current - 1) * limit;
        return baseMapper.findUserNews(current , limit , userId);
    }

    //让系统消息已读
    @Async
    @Override
    public void setSystemNewsRead(List<SystemVo> systemVos) {
        log.info("让系统消息已读");
        List<String> idList = new ArrayList<>();
        for(SystemVo systemVo : systemVos){
            idList.add(systemVo.getId());
        }
        if(idList.size() != 0){
            baseMapper.setSystemNewsRead(idList);
        }
    }

}
