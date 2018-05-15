package com.smzdm.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.smzdm.util.SmzdmLocalTimeDeserializer;
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

    @JSONField(name = "article_id")
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    @JSONField(name = "article_title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JSONField(name = "article_price")
    public void setPrice(String price) {
        this.price = price;
    }

    @JSONField(name = "article_pic")
    public void setPic(String pic) {
        this.pic = pic;
    }

    @JSONField(name = "article_url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JSONField(name = "article_mall")
    public void setMall(String mall) {
        this.mall = mall;
    }

    @JSONField(name = "article_comment")
    public void setComment(Short comment) {
        this.comment = comment;
    }

    @JSONField(name = "article_collection")
    public void setCollection(Short collection) {
        this.collection = collection;
    }

    @JSONField(name = "article_worthy")
    public void setWorthy(Short worthy) {
        this.worthy = worthy;
    }

    @JSONField(name = "article_unworthy")
    public void setUnworthy(Short unworthy) {
        this.unworthy = unworthy;
    }

    @JSONField(name = "article_channel_name")
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @JSONField(deserializeUsing = SmzdmLocalTimeDeserializer.class, name = "publish_date_lt")
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}