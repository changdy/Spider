package com.smzdm.mapper;


import com.smzdm.pojo.Category;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CategoryMapper extends BaseCategoryMapper {
    void insertList(Collection<Category> categories);

    @Select("SELECT ID FROM category;")
    Long[] getIDArray();

    @Select("SELECT title,id FROM category;")
    List<Map<String, Object>> getCategoryMap();
}