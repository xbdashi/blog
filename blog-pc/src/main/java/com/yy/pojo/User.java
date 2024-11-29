package com.yy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String photo;

    private String phone;

    private Integer isIdentity;
    private Integer provinceId;
    private Integer cityId;
    private Integer distractId;
    private Integer isDelete;

    private Integer version;

    private String fans;

    private String focus;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
