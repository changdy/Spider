package com.smzdm.mapper;


import com.smzdm.model.ArticleModel;
import com.smzdm.model.ArticleSearch;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ArticleMapper extends BaseArticleMapper {

    int getCount(ArticleSearch articleSearch);

    List<ArticleModel> queryArticle(ArticleSearch articleSearch);

}