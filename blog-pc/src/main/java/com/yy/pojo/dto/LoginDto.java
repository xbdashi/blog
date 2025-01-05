package com.yy.pojo.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class LoginDto {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @Email
    private String email;
    private String emailCode; // qq邮箱验证码
    private String code; // 验证码
}
