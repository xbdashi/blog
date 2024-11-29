package com.yy.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "标签分页实体属性类")
public class SignPageDto {
    @ApiModelProperty(value = "页码",required = true)
    private Integer pageNum;
    @ApiModelProperty(value = "大小",required = true)
    private Integer pageSize;
}
