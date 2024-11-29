package com.yy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yy.pojo.EssayCategories;
import com.yy.service.EssayCategoriesService;
import com.yy.mapper.EssayCategoriesMapper;
import org.springframework.stereotype.Service;

/**
* @author Max
* @description 针对表【essay_categories(存储文章与分类的多对多关系)】的数据库操作Service实现
* @createDate 2024-11-25 16:30:53
*/
@Service
public class EssayCategoriesServiceImpl extends ServiceImpl<EssayCategoriesMapper, EssayCategories>
    implements EssayCategoriesService{

}




