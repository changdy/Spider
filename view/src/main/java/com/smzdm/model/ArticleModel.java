package com.smzdm.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleModel {
    private Integer articleId;
    private String title;
    private String content;
    private String type;
    private String price;
    private String yhType;
    private String pic;
    private String url;
    private String brand;
    private String mall;
    private String channel;
    private LocalDateTime date;
    private Short comment;
    private Short collection;
    private Short worthy;
    private Short unworthy;
    private Boolean soldOut;
    private Boolean timeout;
    private String categoryTitle;
}