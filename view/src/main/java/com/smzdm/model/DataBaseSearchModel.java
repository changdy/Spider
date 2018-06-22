package com.smzdm.model;

import lombok.Data;

/**
 * Created by Changdy on 2018/3/26.
 */
@Data
public class DataBaseSearchModel extends TimePageModel {
    private String keyWords;
    private Short[] secondCategory;
    private Short[] thirdCategory;
    // 多个关键字 需要用&分割
    private Short worthy;
    private Short comment;
}