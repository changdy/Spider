package com.smzdm.mapper;


import com.smzdm.pojo.Category;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Collection;
import java.util.List;

public interface CategoryMapper extends BaseCategoryMapper {
    void insertList(Collection<Category> categories);

    @Select("SELECT title,id FROM category;")
    List<Category> getCategoryArray();

    @Update(" truncate table category")
    void truncateCategory();
}