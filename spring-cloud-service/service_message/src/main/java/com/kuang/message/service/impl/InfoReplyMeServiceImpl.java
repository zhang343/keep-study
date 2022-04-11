package com.kuang.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.message.client.UcenterClient;
import com.kuang.message.entity.InfoReplyMe;
import com.kuang.message.entity.vo.ReplyMeVo;
import com.kuang.message.mapper.InfoReplyMeMapper;
import com.kuang.message.service.InfoReplyMeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.springcloud.utils.VipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
@Service
@Slf4j
public class InfoReplyMeServiceImpl extends ServiceImpl<InfoReplyMeMapper, InfoReplyMe> implements InfoReplyMeService {

    @Resource
    private UcenterClient ucenterClient;

    //查找未读消息
    @Override
    public Integer findUserUnreadNumber(String userId) {
        QueryWrapper<InfoReplyMe> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.eq("is_read" , 0);
        return baseMapper.selectCount(wrapper);
    }

    //查询回复我的消息数量
    @Override
    public Integer findUserNewsNumber(String userId) {
        QueryWrapper<InfoReplyMe> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }

    //查询回复我的消息
    @Override
    public List<ReplyMeVo> findUserNews(Long current, Long limit, String userId) {
        current = (current - 1) * limit;
        List<ReplyMeVo> userNews = baseMapper.findUserNews(current, limit, userId);
        if(userNews == null || userNews.size() == 0){
            return userNews;
        }
        VipUtils.setVipLevel(userNews , userNews.get(0) , "setVipLevel" , "getReplyUserId");
        return userNews;
    }

    //设置回复我的消息已读
    @Async
    @Override
    public void setReplyMeRead(List<ReplyMeVo> replyMeVos) {
        log.info("让回复我的消息已读");
        List<String> idList = new ArrayList<>();
        for(ReplyMeVo replyMeVo : replyMeVos){
            idList.add(replyMeVo.getId());
        }
        if(idList.size() != 0){
            baseMapper.setReplyMeRead(idList);
        }
    }

    //删除用户回复消息
    @Override
    public void delete(String id, String userId) {
        QueryWrapper<InfoReplyMe> wrapper = new QueryWrapper<>();
        wrapper.eq("id" , id);
        wrapper.eq("user_id" , userId);
        int delete = baseMapper.delete(wrapper);
        if(delete != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "删除失败");
        }
    }

    //回复用户消息
    @Async
    @Override
    public void addreply(String id, String content, String userId) {
        QueryWrapper<InfoReplyMe> wrapper = new QueryWrapper<>();
        wrapper.eq("id" , id);
        wrapper.eq("user_id" , userId);
        InfoReplyMe infoReplyMe = baseMapper.selectOne(wrapper);


        InfoReplyMe infoReplyMe1 = new InfoReplyMe();
        infoReplyMe1.setUserId(infoReplyMe.getReplyUserId());
        infoReplyMe1.setArticleId(infoReplyMe.getArticleId());
        infoReplyMe1.setTitle(infoReplyMe.getTitle());
        infoReplyMe1.setReplyUserId(userId);
        infoReplyMe1.setContent(content);

        R avatarAndNicknameByUserId = ucenterClient.findAvatarAndNicknameByUserId(userId);
        if(!avatarAndNicknameByUserId.getSuccess()){
            return;
        }
        String avatar = (String) avatarAndNicknameByUserId.getData().get("avatar");
        String nickname = (String) avatarAndNicknameByUserId.getData().get("nickname");
        infoReplyMe1.setReplyUserAvatar(avatar);
        infoReplyMe1.setReplyUserNickname(nickname);
        baseMapper.insert(infoReplyMe1);
    }
}
