package com.yy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName categories
 */
@TableName(value ="categories")
@Data
public class Categories implements Serializable {
    private Integer id;

    private String name;

    private String description;

    private Date createdTime;

    private Date updatedTime;

    private static final long serialVersionUID = 1L;
}