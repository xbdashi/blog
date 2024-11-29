package com.yy.service;

import com.yy.pojo.Essay;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yy.pojo.dto.EssayPageDto;
import com.yy.pojo.vo.EssayVo;
import com.yy.utils.PageResult;
import com.yy.utils.Result;

import java.util.List;

/**
* @author Max
* @description 针对表【essay】的数据库操作Service
* @createDate 2024-11-25 16:30:53
*/
public interface EssayService extends IService<Essay> {

    // 查询文章并且把标签也查出来
    PageResult pageWithSign(EssayPageDto essayPageDto);
    // 查询最热文章
    List<Essay> hot();

    // 查询最新文章
    List<Essay> latest();

    int insertEssay(Essay essay);

}
