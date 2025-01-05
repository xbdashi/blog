package com.yy.exception;

import com.sun.mail.smtp.SMTPSendFailedException;
import com.yy.constants.ExceptionStatus;
import com.yy.utils.Result;
import org.springframework.core.NestedRuntimeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(BaseException.class)
    public Result<String> error(BaseException baseException){
        return Result.error(baseException.getCode(),baseException.getMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> error(MethodArgumentNotValidException baseException){
        return Result.error(501,"请输入相应字段");
    }
}
