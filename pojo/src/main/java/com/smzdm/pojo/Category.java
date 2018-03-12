package com.smzdm.pojo;

import com.smzdm.Handler;
import com.smzdm.HandlerFunction;
import lombok.Data;

@Data
public class Category {
    @Handler("ID")
    private Short id;
    @Handler("title")
    private String title;
    @Handler( value = "parent_ids", function = HandlerFunction.PARENTS)
    private Short[] parentIds;
    @Handler("url_nicktitle")
    private String nickTitle;
}