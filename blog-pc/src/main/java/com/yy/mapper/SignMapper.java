package com.yy.mapper;

import com.yy.pojo.Sign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Max
* @description 针对表【sign】的数据库操作Mapper
* @createDate 2024-11-25 16:30:53
* @Entity com.yy.pojo.Sign
*/
public interface SignMapper extends BaseMapper<Sign> {

    List<Sign> selectTop8();

}




