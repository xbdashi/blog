package com.yy.exception;

public class SignExistsException extends BaseException{
    public SignExistsException(Integer code, String msg) {
        super(code, msg);
    }
}
