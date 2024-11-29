package com.yy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yy.pojo.Comment;
import com.yy.service.CommentService;
import com.yy.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
* @author Max
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2024-11-25 16:30:53
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




