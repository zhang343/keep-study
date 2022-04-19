package com.kuang.bbs.mapper;

import com.kuang.bbs.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.bbs.entity.vo.OneCommentVo;
import com.kuang.bbs.entity.vo.TwoCommentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CommentMapper extends BaseMapper<Comment> {

    //查找一级评论,分页查询
    List<OneCommentVo> findOneCommentVoByArticleId(@Param("articleId") String articleId ,
                                                   @Param("current") Long current ,
                                                   @Param("limit") Long limit);

    //查找二级评论
    List<TwoCommentVo> findTwoCommentVoByFatherId(String fatherId);
}
