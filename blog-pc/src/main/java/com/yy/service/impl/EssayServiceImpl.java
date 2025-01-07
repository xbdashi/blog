package com.yy.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yy.enums.EssayStatus;
import com.yy.enums.SortEnums;
import com.yy.mapper.CommentMapper;
import com.yy.mapper.RelationMapper;
import com.yy.mapper.UserMapper;
import com.yy.pojo.*;
import com.yy.pojo.dto.EssayPageDto;
import com.yy.pojo.vo.CommentVo;
import com.yy.pojo.vo.EssayVo;
import com.yy.pojo.vo.UserVo;
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

import java.time.LocalDateTime;
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
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private RelationMapper relationMapper;
    @Override
    public PageResult pageWithSign(EssayPageDto essayPageDto) {
        Page<Essay> iPage = new Page<>(essayPageDto.getPageNum(), essayPageDto.getPageSize());
        QueryWrapper<Essay> qw = new QueryWrapper<>();
        qw.lambda().like(Essay::getTitle, essayPageDto.getTitle()).orderByDesc(Essay::getCreateTime);
        qw = new QueryWrapper<>();
        if(Objects.equals(essayPageDto.getSort().getCode(), SortEnums.HOT.getCode())){
            qw.lambda().orderByDesc(Essay::getViews);
        }else{
            // 以创建时间排asc
            qw.lambda().orderByAsc(Essay::getCreateTime);
        }
        Page<Essay> page = page(iPage,qw); // 获取到分页对象
        List<EssayVo> list = new ArrayList<>();
        Optional.ofNullable(page).orElse(new Page<>())
                .getRecords()
                .stream()
                .forEach(essay -> {
                    EssayVo essayVo = new EssayVo();
                    UserVo userVo = new UserVo();
                    BeanUtils.copyProperties(essay, essayVo);
                    // 根据文章id和用户id查询对应的用户
                    User user = selectUser(essay.getUserId());
                    BeanUtils.copyProperties(user,userVo);
                    userVo.setFanIds(getFansIds(essay.getUserId()));
                    essayVo.setUser(userVo);
                    essayVo.setSignsList(essaySignService.getWithSign(essay.getId()));
                    // 根据文章id查询对应的评论
                    essayVo.setCommentNum(essay.getCommentNum());
                    list.add(essayVo);
                });

        return new PageResult(page.getTotal(), list);
    }

    private User selectUser( Long userId) {
        QueryWrapper<User> wq =new QueryWrapper<>();
        wq.lambda().eq(User::getId, userId);
        return userMapper.selectOne(wq);
    }

    // 查询用户表。根据用户查询对应的用户分时id
    private List<Long> getFansIds(Long userId) {
        QueryWrapper<Relation> wq =new QueryWrapper<>();
        wq.lambda().eq(Relation::getFocusedId, userId);
        List<Relation> relations = relationMapper.selectList(wq);
        List<Long> fansIds = new ArrayList<>();
        Optional.ofNullable(relations).orElse(new ArrayList<>())
                .stream()
                .forEach(item -> {
                    fansIds.add(item.getFocusId());
                });
        return fansIds;
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
        essay.setId(essay.getStatus());
        // 喜欢 视图 评论数 全部为0
        essay.setLikes(0L);
        essay.setViews(0L);
        essay.setCommentNum(0L);
        essay.setCreateTime(LocalDateTime.now());
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

    @Override
    public List<CommentVo> selectById(Long essayId) {
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.lambda().eq(Comment::getEssayId, essayId).orderByDesc(Comment::getLikes).isNull(Comment::getParentId);
        List<Comment> list = commentMapper.selectList(commentQueryWrapper);
        return getCommentVos(list);
    }

    private List<CommentVo> getCommentVos(List<Comment> list) {
        // 返回的列表
        List<CommentVo> voList = new ArrayList<>();
        list.forEach(item -> {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(item, commentVo);
            commentVo.setUser(userMapper.selectById(item.getUserId()));
            // 通过评论id将所有的子评论查询出来
            QueryWrapper<Comment> childQueryWrapper = new QueryWrapper<>();
            childQueryWrapper.lambda().eq(Comment::getParentId, item.getId());
            List<Comment> list1 = commentMapper.selectList(childQueryWrapper);
            if(!Objects.isNull(list1)){
               commentVo.setChildren(getCommentVos(list1));
            }
            voList.add(commentVo);
        });
        return voList;
    }

}




