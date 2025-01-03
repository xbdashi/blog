package com.yy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName essay_categories
 */
@TableName(value ="essay_categories")
@Data
public class EssayCategories implements Serializable {
    private Integer id;

    private Integer essayId;

    private Integer categoryId;

    private static final long serialVersionUID = 1L;
}