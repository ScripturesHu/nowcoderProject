package com.hjw;

import com.hjw.community.ExerApplication;
import com.hjw.community.util.CommunityUtil;
import com.hjw.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author hujw
 * @description
 * @create 2021-10-08 14:52
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ExerApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;



    @Test
    public void testTextMail(){
        mailClient.sendMail("1796386219@qq.com","TEST","welcome");
    }

    //使用thymeleaf模板引擎发送html格式邮件示例
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username","sunday");
        String content = templateEngine.process("/mail/demo",context);
        System.out.println(content);

        mailClient.sendMail("1796386219@qq.com", "html", content);
    }

    @Test
    public void test1(){
        String s = CommunityUtil.md5("123" + "2be73");
        System.out.println(s);
    }
}
