package com.smzdm.pojo;

import lombok.Data;

@Data
public class Category {
    private Short id;
    private String title;
    private Integer[] parentIds;
    private String nickTitle;
}