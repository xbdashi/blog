package com.yy.utils;

import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.SensitiveUtil;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.yy.enums.UserStatus;
import com.yy.exception.FreezeException;
import com.yy.exception.SensitivityException;
import com.yy.pojo.Sensitivity;
import com.yy.pojo.User;
import com.yy.service.SensitivityService;
import com.yy.service.UserService;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
public class SensitivityFilter {
    @Value("${blog.maxCount}")
    private int maxCount;
    @Autowired
    private UserService userService;
    public void filterMessage(String text) {
        if (maxCount <= 0) {
            throw new FreezeException(451,"账户被冻结");
        }
        if (SensitiveWordHelper.contains(text)) {
            throw new SensitivityException(450,"请友好评论噢,"+(--maxCount)+"之后账户被冻结");
        }
        maxCount = 3;
    }
}
