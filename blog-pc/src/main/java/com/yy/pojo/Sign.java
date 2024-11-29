package com.yy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @TableName sign
 */
@TableName(value ="sign")
@Data
@ApiModel(description = "标签")
public class Sign implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("标签名")
    private String label;
    @ApiModelProperty("展示的颜色")
    private String type;
    @TableField(exist = false) // 不映射数据库字段
    @ApiModelProperty("统计次数")
    @JsonIgnore
    private Long count;
    private static final long serialVersionUID = 1L;
}
