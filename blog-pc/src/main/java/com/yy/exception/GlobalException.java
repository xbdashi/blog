package com.yy.exception;

import com.yy.utils.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(BaseException.class)
    public Result<String> error(BaseException baseException){
        return Result.error(baseException.getCode(),baseException.getMsg());
    }
}
