package com.smzdm.mapper;

import com.smzdm.pojo.Article;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface ArticleMapper extends BaseArticleMapper {
    void deleteByIDList(List<Integer> ids);

    void insertList(List<Article> articles);

    @Select("SELECT price, title, mall, category FROM article WHERE article_id =#{id}")
    Article getMainInfo(Integer id);
}