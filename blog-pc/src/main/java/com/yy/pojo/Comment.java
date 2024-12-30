package com.yy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String essayId;

    private String comment;

    private LocalDateTime createTime;

    private Long likes;
    // 评论用户的id
    private Long userId;
    private String isDelete;
    private String parentId;
    private static final long serialVersionUID = 1L;
}
