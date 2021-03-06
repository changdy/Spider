package com.smzdm.mapper;

import com.smzdm.pojo.ArticleInfo;

import java.util.List;

public interface BaseArticleInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.article_info
     *
     * @mbg.generated Fri Jan 26 22:47:41 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.article_info
     *
     * @mbg.generated Fri Jan 26 22:47:41 CST 2018
     */
    int insert(ArticleInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.article_info
     *
     * @mbg.generated Fri Jan 26 22:47:41 CST 2018
     */
    ArticleInfo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.article_info
     *
     * @mbg.generated Fri Jan 26 22:47:41 CST 2018
     */
    List<ArticleInfo> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.article_info
     *
     * @mbg.generated Fri Jan 26 22:47:41 CST 2018
     */
    int updateByPrimaryKey(ArticleInfo record);
}