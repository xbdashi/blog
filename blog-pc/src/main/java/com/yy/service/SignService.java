package com.yy.service;

import com.yy.pojo.Sign;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Max
* @description 针对表【sign】的数据库操作Service
* @createDate 2024-11-25 16:30:53
*/
public interface SignService extends IService<Sign> {

    List<Sign> selectSign();

}
