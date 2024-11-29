package com.yy.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yy.enums.EssayStatus;
import com.yy.mapper.UserMapper;
import com.yy.pojo.Essay;
import com.yy.pojo.EssaySign;
import com.yy.pojo.Sign;
import com.yy.pojo.dto.EssayPageDto;
import com.yy.pojo.vo.EssayVo;
import com.yy.service.EssayService;
import com.yy.mapper.EssayMapper;
import com.yy.service.EssaySignService;
import com.yy.service.SignService;
import com.yy.utils.PageResult;
import com.yy.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author Max
* @description 针对表【essay】的数据库操作Service实现
* @createDate 2024-11-25 16:30:53
*/
@Service
public class EssayServiceImpl extends ServiceImpl<EssayMapper, Essay>
    implements EssayService{
    @Autowired
    private EssaySignService essaySignService;
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult pageWithSign(EssayPageDto essayPageDto) {
        Page<Essay> iPage = new Page<>(essayPageDto.getPageNum(), essayPageDto.getPageSize());
        QueryWrapper<Essay> qw = new QueryWrapper<>();
        qw.lambda().like(Essay::getTitle, essayPageDto.getTitle()).orderByDesc(Essay::getCreateTime);
        Page<Essay> page = page(iPage,qw); // 获取到分页对象
        List<EssayVo> list = new ArrayList<>();
        Optional.ofNullable(page).orElse(new Page<>())
                .getRecords()
                .stream()
                .forEach(essay -> {
                    EssayVo essayVo = new EssayVo();
                    BeanUtils.copyProperties(essay, essayVo);
                    essayVo.setUser(userMapper.selectById(essay.getUserId()));
                    essayVo.setSignsList(essaySignService.getWithSign(essay.getId()));
                    list.add(essayVo);
                });

        return new PageResult(page.getTotal(), list);
    }

    @Override
    public List<Essay> hot() {
        QueryWrapper<Essay> qw = new QueryWrapper<>();
        qw.lambda().orderByDesc(Essay::getViews).last("limit 6");
        return this.baseMapper.selectList(qw);
    }

    @Override
    public List<Essay> latest() {
        QueryWrapper<Essay> qw = new QueryWrapper<>();
        qw.lambda().orderByDesc(Essay::getCreateTime).last("limit 6");
        return this.baseMapper.selectList(qw);
    }

    @Override
    @Transactional
    public int insertEssay(Essay essay) {
        // 默认状态为 审核中
        essay.setStatu(EssayStatus.REVIEWING.getCode());
        // 喜欢 视图 评论数 全部为0
        essay.setLikes(0L);
        essay.setViews(0L);
        essay.setCommentNum(0L);
        this.baseMapper.insert(essay);
        // 插入表
        int insert = this.baseMapper.insert(essay);
        List<Long> signIds = Arrays.stream(essay.getLabelIds()).collect(Collectors.toList());
        // 插入中间表
        // 存放临时对象 EssaySign 方便批量插入
        List<EssaySign> essaySigns = new ArrayList<>();
        signIds.stream()
                .forEach(item ->{
                    EssaySign essaySign = new EssaySign();
                    essaySign.setEssayId(essay.getId());
                    essaySign.setSignId(item);
                    essaySigns.add(essaySign);
                });
        // 批量插入
        essaySignService.saveBatch(essaySigns);
        return insert > 0 ? 1 : 0;
    }
}




