package com.yy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yy.pojo.Message;
import com.yy.service.MessageService;
import com.yy.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
* @author Max
* @description 针对表【message】的数据库操作Service实现
* @createDate 2025-01-03 08:49:57
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

}




