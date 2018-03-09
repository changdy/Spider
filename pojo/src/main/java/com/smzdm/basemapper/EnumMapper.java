package com.smzdm.basemapper;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Update("ALTER TYPE ${enumName} ADD VALUE ${item};")
    int addEnum(@Param("enumName") String enumName, @Param("item") String item);
}
