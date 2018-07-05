package com.smzdm.service;

import com.alibaba.fastjson.JSON;
import com.smzdm.HandlerFunction;
import com.smzdm.config.ProjectConfig;
import com.smzdm.mapper.CategoryMapper;
import com.smzdm.model.CategoryModel;
import com.smzdm.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    @Autowired
    private ValueOperations<String, String> valueOperations;
    @Autowired
    private ProjectConfig projectConfig;
    @Autowired
    private List<Category> categories;

    public void insert(String categoryString) {
        List<CategoryModel> categoryModels = JSON.parseArray(categoryString, CategoryModel.class);
        List<CategoryModel> parallelList = convertToParallelList(categoryModels, new ArrayList<>(2500));
        List<Category> collect = parallelList.stream().map(x -> {
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
        categories.clear();
        categories.addAll(collect);
        categoryMapper.insertList(collect);
        valueOperations.set(projectConfig.getCategoryKey(), getTopThree(categoryModels));
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

    private String getTopThree(List<CategoryModel> categoryModels) {
        categoryModels.forEach(x -> {
            x.setParentIds(null);
            List<CategoryModel> secondLayer = x.getChild();
            if (!CollectionUtils.isEmpty(secondLayer)) {
                secondLayer.forEach(y -> {
                    y.setParentIds(null);
                    List<CategoryModel> thirdLayer = y.getChild();
                    if (!CollectionUtils.isEmpty(thirdLayer)) {
                        thirdLayer.forEach(z -> {
                            z.setChild(null);
                            z.setParentIds(null);
                        });
                    }
                });
            }
        });
        return JSON.toJSONString(categoryModels);
    }
}