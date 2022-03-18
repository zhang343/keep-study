package com.kuang.ucenter.service;

import com.kuang.ucenter.entity.UserAttention;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.ucenter.entity.vo.UserVo;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-02-05
 */
public interface UserAttentionService extends IService<UserAttention> {

    //查询出指定用户的所有粉丝id
    List<String> findUserFansId(String userId);


}
