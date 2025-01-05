package com.yy.exception;

public class JwtException extends BaseException{
    public JwtException(Integer code, String msg) {
        super(code, msg);
    }
}
