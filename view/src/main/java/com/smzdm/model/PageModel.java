package com.smzdm.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Changdy on 2018/3/26.
 */
@Data
public class PageModel {
    @NotNull
    private Integer pageIndex;
    @NotNull
    private Integer pageSize;
    private Integer offset;
    //order by 字段
    private String orderBy;
    // 表示正反序
    private Boolean desc;
}
