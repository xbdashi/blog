package com.yy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yy.pojo.Categories;
import com.yy.service.CategoriesService;
import com.yy.mapper.CategoriesMapper;
import org.springframework.stereotype.Service;

/**
* @author Max
* @description 针对表【categories(存储博客文章的分类信息)】的数据库操作Service实现
* @createDate 2024-11-25 16:30:53
*/
@Service
public class CategoriesServiceImpl extends ServiceImpl<CategoriesMapper, Categories>
    implements CategoriesService{

}




