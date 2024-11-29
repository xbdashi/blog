package com.yy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
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
    private Long disLikes;
    // 评论用户的id
    private Long userId;
    private String isDelete;

    // 当前用户的信息
    @TableField(exist = false)
    private User user;
    private static final long serialVersionUID = 1L;
}
