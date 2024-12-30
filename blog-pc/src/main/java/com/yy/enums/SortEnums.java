package com.yy.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortEnums {
    HOT(1L, "最热"),
    FRESH(2L, "最新");

    @EnumValue // 存入数据库的值
    @JsonValue // 标记响应json值
    private final Long code;
    private final String value;


}
