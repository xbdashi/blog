package com.yy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName essay_sign
 */
@TableName(value ="essay_sign")
@Data
public class EssaySign implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long signId;

    private Long essayId;

    private static final long serialVersionUID = 1L;
}
