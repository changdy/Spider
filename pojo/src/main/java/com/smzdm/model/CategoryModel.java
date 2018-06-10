package com.smzdm.model;

import lombok.Data;

import java.util.List;

@Data
public class CategoryModel {
    private Short id;
    private String title;
    private String parentIds;
    private String urlNicktitle;
    private List<CategoryModel> child;
}