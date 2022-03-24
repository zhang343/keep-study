package com.kuang.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.ucenter.entity.UserTalk;
import com.kuang.ucenter.entity.vo.OtherUserTalkVo;
import com.kuang.ucenter.entity.vo.UserTalkVo;
import com.kuang.ucenter.mapper.UserTalkMapper;
import com.kuang.ucenter.service.UserTalkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
@Service
@Slf4j
public class UserTalkServiceImpl extends ServiceImpl<UserTalkMapper, UserTalk> implements UserTalkService {

    //查询用户说说数量
    @Override
    public Integer findUserTalkNumber(String userId) {
        QueryWrapper<UserTalk> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        return baseMapper.selectCount(wrapper);
    }

    //查找用户说说
    @Override
    public List<UserTalkVo> findUserTalk(String userId, Long current, Long limit) {
        current = (current - 1) * limit;
        return baseMapper.findUserTalk(userId , current , limit);
    }

    //用户发表说说
    @Override
    public UserTalkVo publishTalk(String userId, String content) {
        UserTalk userTalk = new UserTalk();
        userTalk.setUserId(userId);
        userTalk.setContent(content);
        int insert = baseMapper.insert(userTalk);
        if(insert != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "发表说说失败");
        }
        UserTalkVo userTalkVo = new UserTalkVo();
        BeanUtils.copyProperties(userTalk , userTalkVo);
        userTalkVo.setIsPublic(true);
        return userTalkVo;
    }

    //删除用户说说
    @Override
    public void deleteUserTalk(String userId, String id) {
        QueryWrapper<UserTalk> wrapper = new QueryWrapper<>();
        wrapper.eq("id" , id);
        wrapper.eq("user_id" , userId);
        int delete = baseMapper.delete(wrapper);
        if(delete != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "删除失败");
        }
    }

    //修改用户说说是否可以公开
    @Override
    public void updateUserTalkIsPublic(String userId, String id, Boolean isPublic) {
        QueryWrapper<UserTalk> wrapper = new QueryWrapper<>();
        wrapper.eq("id" , id);
        wrapper.eq("user_id" , userId);
        UserTalk userTalk = new UserTalk();
        userTalk.setIsPublic(isPublic);
        int update = baseMapper.update(userTalk, wrapper);
        if(update != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "修改失败");
        }
    }

    //查询他人用户说说数量
    @Override
    public Integer findOtherUserTalkNumber(String userId) {
        QueryWrapper<UserTalk> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id" , userId);
        wrapper.eq("is_public" , 1);
        return baseMapper.selectCount(wrapper);
    }

    //查询他人用户说说
    @Override
    public List<OtherUserTalkVo> findOtherUserTalk(String userId, Long current, Long limit) {
        current = (current - 1) * limit;
        return baseMapper.findOtherUserTalk(userId , current , limit);
    }
}
