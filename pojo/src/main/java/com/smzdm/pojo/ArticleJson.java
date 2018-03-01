package com.smzdm.pojo;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleJson {
    private Long id;
    private String content;
    private Boolean isDiscovery;
    private LocalDateTime createDate;
}