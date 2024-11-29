package com.yy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yy.pojo.Identity;
import com.yy.service.IdentityService;
import com.yy.mapper.IdentityMapper;
import org.springframework.stereotype.Service;

/**
* @author Max
* @description 针对表【identity】的数据库操作Service实现
* @createDate 2024-11-25 16:30:53
*/
@Service
public class IdentityServiceImpl extends ServiceImpl<IdentityMapper, Identity>
    implements IdentityService{

}




