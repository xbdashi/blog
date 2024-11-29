package com.yy.controller.pc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yy.constants.ExceptionStatus;
import com.yy.exception.SignExistsException;
import com.yy.pojo.Sign;
import com.yy.pojo.dto.SignPageDto;
import com.yy.service.SignService;
import com.yy.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sign")
@Api(tags = "标签接口")
@Slf4j
public class SignController {
    @Autowired
    private SignService signService;
    // 查询前八个标签
    @PostMapping("/page")
    @ApiOperation(value = "查询所有")
    public Result<List<Sign>> selectSign(@RequestBody SignPageDto signPageDto){
        log.info("查询所有的标签：{}",signPageDto);
        IPage<Sign> signIPage = new Page<>(signPageDto.getPageNum(), signPageDto.getPageSize());
        IPage<Sign> page = signService.page(signIPage);
        return Result.success(page.getRecords());
    }

    // 添加标签
    @PostMapping
    @ApiModelProperty(value = "添加标签")
    public Result<String> save(@RequestBody Sign sign){
        // 通过名字查询 如果有就报错
        QueryWrapper<Sign> qw = new QueryWrapper<>();
        qw.lambda().eq(Sign::getLabel,sign.getLabel());
        Sign one = signService.getOne(qw);
        if(one != null){
            throw new SignExistsException(ExceptionStatus.SIGN_EXISTS_CODE,"标签已存在");
        }
        signService.save(sign);
        return Result.success(200,"添加成功",null);
    }
}
