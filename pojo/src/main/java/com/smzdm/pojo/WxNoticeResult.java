package com.smzdm.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by Changdy on 2018/6/30.
 */
@Data
public class WxNoticeResult {
    private Integer id;
    private String sendMsg;
    private LocalDateTime eventTime;
    private String response;
    private Boolean success;
}
