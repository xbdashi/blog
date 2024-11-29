package com.yy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName identity
 */
@TableName(value ="identity")
@Data
public class Identity implements Serializable {
    private Integer id;

    private Integer userId;

    private String nickName;

    private String card;

    private Integer provinceId;

    private Integer cityId;

    private Integer distractId;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}