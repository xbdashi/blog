package com.yy.exception;

public class AccountException extends BaseException{
    public AccountException(Integer code, String msg) {
        super(code, msg);
    }
}
