package com.smzdm.pojo;


import com.smzdm.Handler;
import com.smzdm.HandlerFunction;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleInfo {
    private Integer id;
    @Handler("article_id")
    private Integer articleId;
    @Handler(value = "article_comment", function = HandlerFunction.SHORT)
    private Short comment = 0;
    @Handler(value = "article_collection", function = HandlerFunction.SHORT)
    private Short collection = 0;
    @Handler(value = "article_worthy|article_rating", function = HandlerFunction.SHORT)
    private Short worthy = 0;
    @Handler(value = "article_unworthy", function = HandlerFunction.SHORT)
    private Short unworthy = 0;
    @Handler("article_is_sold_out|is_out")
    private Boolean soldOut = false;
    @Handler("article_is_timeout|is_timeout")
    private Boolean timeout = false;
    private LocalDateTime updateTime;
}