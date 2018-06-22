package com.smzdm.mapper;


import com.smzdm.model.ArticleModel;
import com.smzdm.model.DataBaseSearchModel;

import java.util.List;

public interface ArticleMapper extends BaseArticleMapper {

    long getCount(DataBaseSearchModel dataBaseSearchModel);

    List<ArticleModel> queryArticle(DataBaseSearchModel dataBaseSearchModel);

}