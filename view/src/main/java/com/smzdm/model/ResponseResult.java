package com.smzdm.model;

import lombok.Data;

/**
 * http请求返回的最外层对象
 */
@Data
public class ResponseResult<T> {
    /**
     * 状态码.
     */
    private Integer code;

    /**
     * 提示信息.
     */
    private String msg;

    /**
     * 返回内容
     */
    private T data;
}
