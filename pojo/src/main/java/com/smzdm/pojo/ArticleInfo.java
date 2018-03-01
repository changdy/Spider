package com.smzdm.pojo;


import com.smzdm.Handler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleInfo {
    private Integer id;
    @Handler("article_id")
    private Long articleId;
    @Handler("article_comment")
    private Short comment;
    @Handler("article_collection")
    private Short collection;
    @Handler("article_worthy")
    private Short worthy;
    @Handler("article_unworthy")
    private Short unworthy;
    @Handler("article_is_sold_out|is_out")
    private Boolean soldOut;
    @Handler("article_is_timeout")
    private Boolean timeout;
    private LocalDateTime updateTime;
}