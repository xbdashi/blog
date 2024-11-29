package com.yy.controller.pc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yy.pojo.Comment;
import com.yy.service.CommentService;
import com.yy.service.UserService;
import com.yy.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论Api")
@Slf4j
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    // 通过文章id查询所有的评论 并且按文章的like数降序排序
    @GetMapping("/{essayId}")
    @ApiOperation("根据文章id查询出对应的评论")
    public Result<List<Comment>> getByEssayId(@PathVariable Long essayId){
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.lambda().eq(Comment::getEssayId,essayId).orderByDesc(Comment::getLikes);
        List<Comment> list = commentService.list(commentQueryWrapper);
        list.forEach(item ->{
            item.setUser(userService.getById(item.getUserId()));
        });
        return Result.success(200,"查询成功",list);
    }
    // 添加评论
    @PostMapping
    @ApiOperation("添加评论")
    public Result add(@RequestBody Comment comment){
        log.info("添加评论{}",comment);
        comment.setCreateTime(LocalDateTime.now());
        commentService.save(comment);
        return Result.success(200,"添加成功",null);
    }
}
