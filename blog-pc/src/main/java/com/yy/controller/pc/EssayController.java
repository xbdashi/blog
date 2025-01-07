package com.yy.controller.pc;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yy.constants.ExceptionStatus;
import com.yy.enums.EssayStatus;
import com.yy.enums.SortEnums;
import com.yy.exception.InsertException;
import com.yy.pojo.Essay;
import com.yy.pojo.EssaySign;
import com.yy.pojo.dto.EssayPageDto;
import com.yy.pojo.vo.CommentVo;
import com.yy.pojo.vo.EssayVo;
import com.yy.service.EssayService;
import com.yy.service.EssaySignService;
import com.yy.utils.PageResult;
import com.yy.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/essay")
@Api(tags = "文章接口")
@Slf4j
public class EssayController {
    @Autowired
    private EssayService essayService;
    @Autowired
    private EssaySignService essaySignService;

    @PostMapping("/page")
    @ApiOperation(value = "分页查询")
    public Result<PageResult> page(@RequestBody EssayPageDto essayPageDto){
        log.info("文章分页数据对象{}",essayPageDto);
        return Result.success(essayService.pageWithSign(essayPageDto));
    }
    // 获取最新或者最热 通过传递过来的参数分辨 1为最热 2为最新
    @GetMapping("/sort")
    // 标记里面的参数有个可以不填
    @ApiOperation(value = "根据参数查询最新或最热", notes = "1为最热 2为最新")
    public Result<List<Essay>> sort(@RequestParam SortEnums sort){
        QueryWrapper<Essay> qw = new QueryWrapper<Essay>();
        // 最热就是以浏览量排倒序
        if(Objects.equals(sort.getCode(), SortEnums.HOT.getCode())){
            qw.lambda().orderByDesc(Essay::getViews);
        }else{
            // 以创建时间排asc
            qw.lambda().orderByAsc(Essay::getCreateTime);
        }
        List<Essay> list = essayService.list(qw).stream().limit(8).collect(Collectors.toList());
        return Result.success(200,"操作成功",list);
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
    // 根据用户id查询对应的文章
    @GetMapping("/{userId}")
    public Result<List<EssayVo>> getEssayByUserId(@PathVariable("userId") Long userId){
        QueryWrapper<Essay> wq = new QueryWrapper<>();
        wq.lambda().eq(Essay::getUserId, userId).orderByDesc(Essay::getCreateTime);
        List<Essay> list = essayService.list(wq);

        List<EssayVo> res = new ArrayList<>();
        list.stream().forEach(item ->{
            EssayVo essayVo = new EssayVo();
            BeanUtils.copyProperties(item,essayVo);
            essayVo.setSignsList(essaySignService.getWithSign(item.getId()));
            res.add(essayVo);
        });
        return Result.success(res);
    }
}
