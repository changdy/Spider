package com.smzdm.handler;


import com.smzdm.enums.ResultEnums;
import com.smzdm.exception.ResponseException;
import com.smzdm.model.ResponseResult;
import com.smzdm.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class ExceptionHandle {


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseResult handle(Exception e) {
        if (e instanceof ResponseException) {
            ResponseException exception = (ResponseException) e;
            log.info(e.getMessage());
            return ResultUtil.error(exception.getCode(), exception.getMessage());
        } else {
            log.error("未被捕捉异常:", e);
            return ResultUtil.error(ResultEnums.UNKNOWN_ERROR.getCode(), ResultEnums.UNKNOWN_ERROR.getMsg() + "[" + e.getClass().getName() + ": " + e.getLocalizedMessage() + "]");
        }
    }
}
