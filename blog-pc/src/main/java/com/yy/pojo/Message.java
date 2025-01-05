package com.yy.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * @TableName message
 */
@TableName(value ="message")
@Data
public class Message implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer userId;

    private String content;
    private LocalDateTime createTime;
    @TableLogic
    private String isDelete;

    private static final long serialVersionUID = 1L;
}
