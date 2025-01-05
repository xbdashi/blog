package com.yy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName sensitivity
 */
@TableName(value ="sensitivity")
@Data
public class Sensitivity implements Serializable {
    private Integer id;

    private String value;

    private static final long serialVersionUID = 1L;
}