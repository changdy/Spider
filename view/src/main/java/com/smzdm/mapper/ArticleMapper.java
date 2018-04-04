package com.smzdm.mapper;


import com.smzdm.model.ArticleSearch;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ArticleMapper extends BaseArticleMapper {

    int getCount(ArticleSearch articleSearch);

    @Select("SELECT count(*) FROM article  WHERE to_tsvector('testzhcfg', title) @@ '${words}'")
    int test(@Param("words") String test);

    @Select("SELECT NOW() >to_date(#{date},'YYYY-MM-DD')")
    boolean testDate(@Param("date") String date);
}