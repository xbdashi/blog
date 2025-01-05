package com.yy.pojo.dto;

import lombok.Data;

@Data
public class FocusDto {
    private Long id; //  关注者
    private Long focusId; // 被关注的id
}
