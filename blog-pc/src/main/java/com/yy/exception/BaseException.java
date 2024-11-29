package com.yy.exception;

import lombok.Data;

@Data
public class BaseException extends RuntimeException {
    private String msg;
    private Integer code;

    public BaseException(Integer code,String msg) {
        this.code = code;
        this.msg = msg;
    }
}
