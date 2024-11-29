package com.yy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yy.pojo.Sign;
import com.yy.service.SignService;
import com.yy.mapper.SignMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* @author Max
* @description 针对表【sign】的数据库操作Service实现
* @createDate 2024-11-25 16:30:53
*/
@Service
public class SignServiceImpl extends ServiceImpl<SignMapper, Sign>
    implements SignService{
    @Autowired
    private SignMapper signMapper;
    @Override
    public List<Sign> selectSign() {
        return signMapper.selectTop8();
    }
}




