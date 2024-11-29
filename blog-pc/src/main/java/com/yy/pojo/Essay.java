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
 * @TableName essay
 */
@TableName(value ="essay")
@Data
public class Essay implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String title;
    private String describes;

    private String content;

    private Long views;
    // 文章状态 0是已保存，1审核中，2 已发布
    private Long statu;
    private Long likes;

    private Long commentNum;

    private Integer isDelete;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private Long[] labelIds;
    private static final long serialVersionUID = 1L;
}
