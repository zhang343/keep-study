package com.kuang.bbs.service;

import java.util.Map;

public interface ESArticleSearchService {

    Map<String , Object> pageArticleCondition(Long current, Long limit, String categoryId, Boolean isExcellentArticle, String articleNameOrLabelName);
}
