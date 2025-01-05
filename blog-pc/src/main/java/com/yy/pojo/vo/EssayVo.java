package com.yy.pojo.vo;

import com.yy.pojo.Essay;
import com.yy.pojo.Sign;
import lombok.Data;

import java.util.List;

@Data
public class EssayVo extends Essay {
    private List<Sign> signsList;

}
