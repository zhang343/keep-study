package com.kuang.bbs.service;

import com.kuang.bbs.entity.Column;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.bbs.entity.vo.ColumnArticleVo;
import com.kuang.bbs.entity.vo.ColumnDetailVo;
import com.kuang.bbs.entity.vo.ColumnVo;
import com.kuang.bbs.entity.vo.UpdateColumnVo;

import java.util.List;
import java.util.concurrent.Future;


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

    //删除专栏，删除专栏也会将下面的文章给删除
    void deleteColumn(String userId, String columnId);

    //修改专栏数据
    void updateColumn(UpdateColumnVo updateColumnVo, String columnId, String userId);

    //设置专栏浏览量
    void setColunmViews(String columnId, String ip);

    //查询出专栏浏览量
    List<Column> findColumnViewsList(List<String> columnIdList);

    //更新专栏浏览量
    void updateColumnViews(List<Column> columnUpdateList);
}
