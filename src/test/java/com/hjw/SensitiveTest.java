package com.hjw;

import com.hjw.community.ExerApplication;
import com.hjw.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author hujw
 * @description
 * @create 2021-10-19 16:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ExerApplication.class)
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter(){
        String text = "这里可以吸毒，可以嫖娼，可以开票！";
        String newText = sensitiveFilter.Filter(text);
        System.out.println(newText);

    }
}
