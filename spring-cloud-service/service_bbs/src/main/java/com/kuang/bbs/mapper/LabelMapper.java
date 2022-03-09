package com.kuang.bbs.mapper;

import com.kuang.bbs.entity.Label;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-02-11
 */
public interface LabelMapper extends BaseMapper<Label> {

    //查询用户标签
    List<Label> findUserLabel(String userId);
}
