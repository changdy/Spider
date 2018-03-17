package com.smzdm.mapper;

import com.smzdm.pojo.ArticleInfo;

import java.util.List;

public interface ArticleInfoMapper extends BaseArticleInfoMapper {

    void insertList(List<ArticleInfo> list);

    void insertHistoryList(List<ArticleInfo> list);

    void deleteByIDArticleIDs(List<Integer> ids);
}