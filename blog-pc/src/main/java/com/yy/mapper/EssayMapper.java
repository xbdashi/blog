package com.yy.mapper;

import com.yy.pojo.Essay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Max
* @description 针对表【essay】的数据库操作Mapper
* @createDate 2024-11-25 16:30:53
* @Entity com.yy.pojo.Essay
*/
public interface EssayMapper extends BaseMapper<Essay> {

    // 根据id查询并且联 essay查询用户信息
    Essay getByIdWithUser(Long id);
}




