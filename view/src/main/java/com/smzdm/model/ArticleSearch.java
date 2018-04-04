package com.smzdm.model;

import lombok.Data;

/**
 * Created by Changdy on 2018/3/26.
 */
@Data
public class ArticleSearch extends TimePageModel {
    private String keyWords;
    private Short[] category;
    private Short worthy;
    private Boolean isDiscovery;
}