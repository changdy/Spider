package com.smzdm.pojo;


import com.smzdm.Handler;
import com.smzdm.HandlerFunction;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Article {
    @Handler("article_id")
    private Integer articleId;
    @Handler("article_title")
    private String title;
    @Handler(value = "article_content", regPattern = "<.+?>")
    private String content;
    @Handler(value = "article_content_all", regPattern = "<.+?>")
    private String contentAll;
    @Handler("article_type")
    private String type;
    @Handler("article_price")
    private String price;
    @Handler("article_yh_type|yh_type")
    private String yhType;
    @Handler("article_pic|article_pic_url")
    private String pic;
    @Handler("article_url")
    private String url;
    @Handler(value = "category_layer", function = HandlerFunction.CATEGORY)
    private Short[] category;
    @Handler("gtm.brand")
    private String brand;
    @Handler(value = "gtm.rmb_price", function = HandlerFunction.PRICE)
    private Double rmbPrice;
    private Boolean isDiscovery;
    @Handler("article_mall")
    private String mall;
    @Handler("article_channel")
    private String channel;
    @Handler("timesort")
    private Long timeSort;
    @Handler(value = "article_date", function = HandlerFunction.DATE)
    private LocalDateTime date;
}