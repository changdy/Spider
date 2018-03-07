package com.smzdm.basemapper;


import org.apache.ibatis.annotations.Select;

/**
 * Created by Changdy on 2018/3/7.
 */
public interface EnumMapper {

    @Select("select unnest( enum_range(null::article_channel)); ")
    String[] getChannels();

    @Select("select unnest( enum_range(null::article_mall)); ")
    String[] getMalls();

    @Select("select unnest( enum_range(null::yh_type)); ")
    String[] getYouHuiTypes();

    @Select("select unnest( enum_range(null::article_type)); ")
    String[] getArticleTypes();
}
