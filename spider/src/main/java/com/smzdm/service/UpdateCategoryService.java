package com.smzdm.service;

import com.alibaba.fastjson.JSON;
import com.smzdm.HandlerFunction;
import com.smzdm.mapper.CategoryMapper;
import com.smzdm.model.CategoryModel;
import com.smzdm.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by Changdy on 2018/4/9.
 */
@Service
public class UpdateCategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public void insert(String json) {
        List<CategoryModel> list = convertToParallelList(JSON.parseArray(json, CategoryModel.class), new ArrayList<>(2500));
        List<Category> collect = list.stream().map(x -> {
            Category category = new Category();
            category.setId(x.getId());
            category.setTitle(x.getTitle());
            category.setNickTitle(x.getUrlNicktitle());
            String parentIds = x.getParentIds();
            if (!parentIds.equals("") && !parentIds.equals("0")) {
                category.setParentIds((Short[]) HandlerFunction.PARENTS.getFunction().apply(parentIds));
            }
            return category;
        }).collect(toList());
        categoryMapper.truncateCategory();
        categoryMapper.insertList(collect);
    }

    private List<CategoryModel> convertToParallelList(List<CategoryModel> categories, List<CategoryModel> parallelList) {
        parallelList.addAll(categories);
        categories.forEach(x -> {
            List<CategoryModel> child = x.getChild();
            if (child != null && child.size() > 0) {
                convertToParallelList(child, parallelList);
            }
        });
        return parallelList;
    }
}