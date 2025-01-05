package com.yy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.*;
import org.springframework.data.annotation.Id;

/**
 * @TableName user
 */
@TableName(value = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String photo;
    private String phone;
    private Long isIdentity;
    private Long provinceId;
    private Long cityId;
    private Long distractId;
    private Long isDelete;
    private String qq;
    private String wchat;
    private Long isLogin;
    private Long version;
    private String fans;
    private String focus;
    private String likes;
    private Date createTime;
    private Date updateTime;
    private static final long serialVersionUID = 1L;
}
