package com.smzdm.mapper;

import com.smzdm.pojo.Article;

import java.util.List;


public interface ArticleMapper extends BaseArticleMapper {
    void deleteByIDList(List<Integer> ids);

    void insertList(List<Article> articles);
}