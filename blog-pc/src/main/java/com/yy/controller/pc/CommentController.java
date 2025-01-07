package com.yy.controller.pc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yy.exception.SensitivityException;
import com.yy.pojo.Comment;
import com.yy.pojo.Essay;
import com.yy.pojo.User;
import com.yy.pojo.vo.CommentVo;
import com.yy.service.CommentService;
import com.yy.service.EssayService;
import com.yy.service.UserService;
import com.yy.utils.Result;
import com.yy.utils.SensitivityFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    @Autowired
    private EssayService essayService;
    @Autowired
    private SensitivityFilter sensitivityFilter;

    // 通过文章id查询所有的评论 并且按文章的like数降序排序
    @GetMapping("/{essayId}")
    @ApiOperation("根据文章id查询出对应的评论")
    public Result<List<CommentVo>> getByEssayId(@PathVariable Long essayId) {
         // 通过文章id查询出所有的文章，以及对应的用户，子评论
        return Result.success(200, "查询成功", essayService.selectById(essayId));
    }

    // 添加评论
    @PostMapping
    @ApiOperation("添加评论")
    public Result add(@RequestBody Comment comment) {
        log.info("添加评论{}", comment);
        comment.setCreateTime(LocalDateTime.now());
        commentService.save(comment);
        try {
            // 如果评论了，但是是敏感词，就抛出异常将内容修改,如果已
            sensitivityFilter.filterMessage(comment.getComment());
        } catch (SensitivityException e) {
            // 删除用户发送的数据。
            commentService.removeById(comment);
            throw new SensitivityException(450,"请友好发言噢"+sensitivityFilter.getMaxCount()+"之后就会冻结账号...");
        }
        // 查询到对应的文章，并且增加评论数
        Essay essay = essayService.getById(comment.getEssayId());
        essay.setCommentNum(essay.getCommentNum() + 1);
        essayService.updateById(essay);
        return Result.success(200, "添加成功", null);
    }

    // 通过parentid查询对应的父username
    @GetMapping("/parent/{parentId}")
    @ApiOperation("通过parentid查询对应的父username")
    public Result getParentUsername(@PathVariable Long parentId) {
        Comment comment = commentService.getById(parentId);
        User user = userService.getById(comment.getUserId());
        return Result.success(200, "查询成功", user.getUsername());
    }
    // 通过文章id查询对应的评论数量
    @GetMapping("/count/{essayId}")
    @ApiOperation("通过文章id查询对应的评论数量")
    public Result getCount(@PathVariable Long essayId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Comment::getEssayId, essayId);
        return Result.success(200, "查询成功", commentService.count(queryWrapper));
    }

}
