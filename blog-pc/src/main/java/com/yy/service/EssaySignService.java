package com.yy.service;

import com.yy.pojo.EssaySign;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yy.pojo.Sign;

import java.util.List;

/**
* @author Max
* @description 针对表【essay_sign】的数据库操作Service
* @createDate 2024-11-25 16:30:53
*/
public interface EssaySignService extends IService<EssaySign> {


    // 通过id查询关系表的数据
    List<Sign> getWithSign(Long id);

}
