package com.yy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName relation
 */
@TableName(value ="relation")
@Data
public class Relation implements Serializable {
    private Integer id;

    private Long focusId; // 关注者id 当前用户id

    private Long focusedId; // 被关注者id

    private static final long serialVersionUID = 1L;
}
