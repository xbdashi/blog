package com.yy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yy.pojo.EssaySign;
import com.yy.pojo.Sign;
import com.yy.service.EssaySignService;
import com.yy.mapper.EssaySignMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* @author Max
* @description 针对表【essay_sign】的数据库操作Service实现
* @createDate 2024-11-25 16:30:53
*/
@Service
public class EssaySignServiceImpl extends ServiceImpl<EssaySignMapper, EssaySign>
    implements EssaySignService{
    @Autowired
    private EssaySignMapper essaySignMapper;
    @Override
    public List<Sign> getWithSign(Long id) {
        return essaySignMapper.selectPageWithSign(id);
    }
}




