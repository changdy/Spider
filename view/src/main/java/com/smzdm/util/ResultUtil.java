package com.smzdm.util;


import com.smzdm.enums.ResultEnums;
import com.smzdm.model.ResponseResult;

public class ResultUtil {

    public static <T> ResponseResult<T> success(T object) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(ResultEnums.SUCCESS.getCode());
        responseResult.setMsg(ResultEnums.SUCCESS.getMsg());
        responseResult.setData(object);
        return responseResult;
    }

    public static ResponseResult success() {
        return success(null);
    }

    public static <T> ResponseResult<T> error(ResultEnums resultEnums) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(resultEnums.getCode());
        responseResult.setMsg(resultEnums.getMsg());
        return responseResult;
    }

    public static ResponseResult error(Integer code, String msg) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(code);
        responseResult.setMsg(msg);
        return responseResult;
    }

    public static <T> ResponseResult<T> error() {
        return error(ResultEnums.UNKNOWN_ERROR);
    }
}