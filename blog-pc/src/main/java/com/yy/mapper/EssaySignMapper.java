package com.yy.mapper;

import com.yy.pojo.EssaySign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yy.pojo.Sign;

import java.util.List;

/**
* @author Max
* @description 针对表【essay_sign】的数据库操作Mapper
* @createDate 2024-11-25 16:30:53
* @Entity com.yy.pojo.EssaySign
*/
public interface EssaySignMapper extends BaseMapper<EssaySign> {

    List<Sign> selectPageWithSign(Long id);
}




