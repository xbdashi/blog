package com.yy.pojo.dto;

import com.yy.enums.SortEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "文章分页实体类")
public class EssayPageDto {
    @ApiModelProperty(value = "页码",required = true)
    private Integer pageNum;
    @ApiModelProperty(value = "大小",required = true)
    private Integer pageSize;
    @ApiModelProperty("标题")
    private String title;  //标题
    @ApiModelProperty("排序")
    private SortEnums sort;  //标题
}
