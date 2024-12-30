package com.yy.pojo.vo;

import com.yy.pojo.Comment;
import com.yy.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo extends Comment {
    private User user;
    private List<CommentVo> children;
}
