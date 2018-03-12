package com.smzdm.pojo;


import com.smzdm.Handler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleInfo {
    private Integer id;
    @Handler("article_id")
    private Integer articleId;
    @Handler("article_comment")
    private Short comment = 0;
    @Handler("article_collection")
    private Short collection = 0;
    @Handler("article_worthy|article_rating")
    private Short worthy = 0;
    @Handler("article_unworthy")
    private Short unworthy = 0;
    @Handler("article_is_sold_out|is_out")
    private Boolean soldOut = false;
    @Handler("article_is_timeout|is_timeout")
    private Boolean timeout = false;
    private LocalDateTime updateTime;
}