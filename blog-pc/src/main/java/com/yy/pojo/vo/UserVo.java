package com.yy.pojo.vo;

import com.yy.pojo.User;
import lombok.*;

import java.util.List;


@Getter
@Setter
public class UserVo extends User {
    private List<Long> fanIds;
}
