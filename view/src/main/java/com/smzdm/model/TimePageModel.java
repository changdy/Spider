package com.smzdm.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Changdy on 2018/3/26.
 */
@Data
public class TimePageModel extends PageModel {
    private String startTime;
    private String endTime;
}
