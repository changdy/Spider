package com.smzdm.pojo;


import com.smzdm.Handler;
import lombok.Data;

import java.time.LocalDateTime;

import static com.smzdm.HandlerFunction.*;

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
    @Handler(value = "category_layer", function = CATEGORY)
    private Short[] category;
    @Handler("gtm.brand")
    private String brand;
    @Handler(value = "gtm.cates_str")
    private String categoryStr;
    @Handler(value = "gtm.rmb_price", function = PRICE)
    private Double rmbPrice;
    private Boolean isDiscovery;
    @Handler("article_mall")
    private String mall;
    @Handler(value = "article_channel",function =CHANNEL )
    private String channel;
    @Handler("timesort")
    private Long timeSort;
    @Handler(value = "article_date", function = DATE)
    private LocalDateTime date;
}