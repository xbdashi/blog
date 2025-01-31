package com.yy.controller.pc;

import cn.hutool.dfa.SensitiveUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.corba.se.spi.orb.Operation;
import com.yy.enums.UserStatus;
import com.yy.exception.BaseException;
import com.yy.exception.FreezeException;
import com.yy.exception.SensitivityException;
import com.yy.pojo.Message;
import com.yy.pojo.User;
import com.yy.pojo.dto.MessagePage;
import com.yy.pojo.vo.MessageVo;
import com.yy.service.MessageService;
import com.yy.service.UserService;
import com.yy.utils.PageResult;
import com.yy.utils.Result;
import com.yy.utils.SensitivityFilter;
import com.yy.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("message")
@Api(tags = "留言板")
@Slf4j

public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private SensitivityFilter sensitivityFilter;
    @PostMapping("/page")
    @ApiOperation("留言板分页查询")
    public Result<PageResult> page(@RequestBody MessagePage messagePage){
        Page<Message> objectPage = new Page<>(messagePage.getPageNum(),messagePage.getPageSize());
        QueryWrapper<Message> qw = new QueryWrapper<>();
        qw.lambda().orderByDesc(Message::getCreateTime); // 倒序排序
        Page<Message> page = messageService.page(objectPage,qw);
        List<Message> records = page.getRecords();
        // 存放MessageVO
        List<MessageVo> list = new ArrayList<>();
        records.forEach(message -> {
            MessageVo messageVo = new MessageVo();
            BeanUtils.copyProperties(message,messageVo);
            // 根据userid查询用户
            messageVo.setUser(userService.getById(message.getUserId()));
            list.add(messageVo);
        });
        PageResult pageResult = new PageResult(page.getTotal(), list);

        return Result.success(pageResult);
    }

    // 添加留言
    @PostMapping
    @ApiOperation("添加留言")
    public Result add(@RequestBody Message message){
        log.info("留言信息{}",message);
        message.setCreateTime(LocalDateTime.now());
        messageService.save(message);
        try {
            // 如果评论了，但是是敏感词，就抛出异常将内容修改,如果已
            sensitivityFilter.filterMessage(message.getContent());

        } catch (SensitivityException e) {
            // 删除用户发送的数据。
            messageService.removeById(message);
            throw new SensitivityException(450,"请友好发言噢"+sensitivityFilter.getMaxCount()+"之后就会冻结账号...");
        }catch (FreezeException e){
            User user = new User();
            user.setIsLogin(UserStatus.LOGOUT.getCode());
            user.setStatus(UserStatus.LOCKED.getCode());
            user.setId(UserContext.getUserId());
            userService.updateById(user);
            messageService.removeById(message);
            throw new SensitivityException(451,"你的账号已经被冻结..");
        }
        return Result.success();
    }
}
