package com.yy.pojo.vo;

import com.yy.pojo.Message;
import com.yy.pojo.User;
import lombok.Data;


@Data
public class MessageVo extends Message {
    private User user;
}
