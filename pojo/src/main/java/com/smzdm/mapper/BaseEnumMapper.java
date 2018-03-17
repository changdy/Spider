package com.smzdm.mapper;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by Changdy on 2018/3/7.
 */
public interface BaseEnumMapper {

    @Select("select unnest( enum_range(null::${name})); ")
    String[] getEnum(@Param("name")String name);

    @Update("ALTER TYPE ${enumName} ADD VALUE ${item};")
    int addEnum(@Param("enumName") String enumName, @Param("item") String item);
}
