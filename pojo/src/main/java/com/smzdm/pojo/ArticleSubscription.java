package com.smzdm.pojo;

import lombok.Data;

/**
 * Created by Changdy on 2018/6/27.
 */
@Data
public class ArticleSubscription {
    private Integer id;
    private String name;
    private String[] titleContain;
    private String[] titleReject;
    private Integer worthCount;
    private Short[] categoryContain;
    private Short[] categoryReject;
}
