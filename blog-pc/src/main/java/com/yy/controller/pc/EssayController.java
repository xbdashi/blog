package com.yy.controller.pc;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yy.constants.ExceptionStatus;
import com.yy.exception.InsertException;
import com.yy.pojo.Essay;
import com.yy.pojo.dto.EssayPageDto;
import com.yy.service.EssayService;
import com.yy.utils.PageResult;
import com.yy.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/essay")
@Api(tags = "文章接口")
@Slf4j
public class EssayController {
    @Autowired
    private EssayService essayService;
    @PostMapping("/page")
    @ApiOperation(value = "分页查询")
    public Result<PageResult> page(@RequestBody EssayPageDto essayPageDto){
        log.info("文章分页数据对象{}",essayPageDto);
        return Result.success(essayService.pageWithSign(essayPageDto));
    }
    // 最热文章
    @PostMapping("/hot")
    @ApiOperation(value = "最热文章")
    public Result<List<Essay>> hot(@RequestBody EssayPageDto essayPageDto){
        IPage<Essay> iPage = new Page<>(essayPageDto.getPageNum(),essayPageDto.getPageSize());
        QueryWrapper<Essay> qw = new QueryWrapper<>();
        qw.lambda().orderByDesc(Essay::getViews);
        IPage<Essay> page = essayService.page(iPage, qw);
        return Result.success(page.getRecords());
    }
    // 最热文章
    @PostMapping("/latest")
    @ApiOperation(value = "最新文章")
    public Result<List<Essay>> latest(@RequestBody EssayPageDto essayPageDto){
        IPage<Essay> iPage = new Page<>(essayPageDto.getPageNum(),essayPageDto.getPageSize());
        QueryWrapper<Essay> qw = new QueryWrapper<>();
        qw.lambda().orderByDesc(Essay::getCreateTime);
        IPage<Essay> page = essayService.page(iPage, qw);
        return Result.success(page.getRecords());
    }

    // 新增和保存
    @PostMapping
    @ApiOperation(value = "新增文章")
    public Result<String> save(@RequestBody Essay essay){
        log.info("新增文章的内容{}",essay);
        // TODO 待做，查询当前创建人的id
        essay.setUserId(essay.getUserId()); // 先默认为1
        int rows = essayService.insertEssay(essay);
        if(rows < 1){
            throw new InsertException(ExceptionStatus.INSERT_EXCEPTION,"插入异常");
        }
        return Result.success();
    }
    // 新增浏览量
    @PostMapping("/view/{id}")
    @ApiOperation(value = "新增浏览量")
    public Result<String> view(@PathVariable(value = "id") Long essayId){
        Essay essay = essayService.getById(essayId);
        essay.setViews(essay.getViews()+1);
        essayService.updateById(essay);
        return Result.success();
    }
}
