package com.smzdm.model;

import lombok.Data;

/**
 * Created by Changdy on 2018/5/10.
 */
@Data
public class SearchType {
    private boolean searchByCategory;
    private String keyWords;
    private Integer page;
}
