package com.kuang.bbs.service;

import com.kuang.bbs.entity.Column;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.bbs.entity.vo.ColumnArticleVo;
import com.kuang.bbs.entity.vo.ColumnDetailVo;
import com.kuang.bbs.entity.vo.ColumnVo;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Xiaozhang
 * @since 2022-03-24
 */
public interface ColumnService extends IService<Column> {

    //创建专栏
    Column addUserColumn(String userId, String nickname , String avatar ,String title);

    //查看用户专栏数量
    Integer findUserColumnNumber(String userId);

    //查看其他用户专栏数量
    Integer findOtherUserColumnNumber(String userId);

    //查询用户专栏
    List<ColumnVo> findUserColumn(String userId);

    //查询他人专栏
    List<ColumnVo> findOtherUserColumn(String userId);

    //校验用户是否可以访问
    Future<Boolean> checkUserAbility(String userId, String columnId);

    //查询专栏具体数据
    ColumnDetailVo findColumnDetail(String columnId);

}
