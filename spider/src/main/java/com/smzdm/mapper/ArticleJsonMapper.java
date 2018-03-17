package com.smzdm.mapper;


import com.smzdm.pojo.ArticleJson;

import java.util.List;

public interface ArticleJsonMapper extends BaseArticleJsonMapper {
    void insertList(List<ArticleJson> list);
}