package com.yy.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "分页结果统一集")
public class PageResult{
    @ApiModelProperty("总条数")
    private Long total;
    @ApiModelProperty("内容")
    private Object rows;
}
