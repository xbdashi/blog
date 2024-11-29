package com.yy.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;


    private static<T> Result<T> build(Integer code, String msg,T data){
       return new Result<T>(code,msg,data);
    }
    public static<T> Result<T> success(){
        return Result.build(200, "操作成功", null);
    }
    public static<T> Result<T> success(T data){
        return Result.build(200, "操作成功", data);
    }
    public static<T> Result<T> success(Integer code, String msg,T data){
        return Result.build(code, msg, data);
    }

    public static<T> Result<T> error(){
        return Result.build(500, "操作失败", null);
    }
    public static<T> Result<T> error(Integer code, String msg){
        return Result.build(code, msg, null);
    }
}
