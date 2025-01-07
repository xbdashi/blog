package com.yy;


import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.SensitiveUtil;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class TestDemo2 {



    @Test
    // 测试敏感词
    public void test8(){
        List<String> list2 = new ArrayList<>();
        list2.add("a");
        list2.add("ab");
        SensitiveUtil.init(list2);
        List<FoundWord> list = SensitiveUtil.getFoundAllSensitive("sbbb", false, true);
        /*list.forEach(e->{
            System.out.println(SensitiveUtil.containsSensitive(e.getFoundWord()));
        });*/
        System.out.println(list);
    }
}
