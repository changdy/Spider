package com.smzdm.exception;


import com.smzdm.enums.ResultEnums;

/**
 * Created by Changdy on 2017/12/2.
 */
public class ResponseException extends RuntimeException {
    private Integer code;

    //只允许通过枚举类初始化code
    public ResponseException(ResultEnums resultEnums) {
        super(resultEnums.getMsg());
        this.code = resultEnums.getCode();
    }

    //只允许通过枚举类初始化code
    public ResponseException(ResultEnums resultEnums, Object customMsg) {
        super(resultEnums.getMsg() + ":" + customMsg.toString());
        this.code = resultEnums.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
