package com.kuang.bbs.mapper;

import com.kuang.bbs.entity.Collect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.bbs.entity.vo.CollectArticleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Xiaozhang
 * @since 2022-03-23
 */
public interface CollectMapper extends BaseMapper<Collect> {

    //查询用户收藏文章
    List<CollectArticleVo> findUserCollectArticle(@Param("userId") String userId,
                                                  @Param("current") Long current,
                                                  @Param("limit") Long limit);
}
