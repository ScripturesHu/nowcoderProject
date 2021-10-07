package com.hjw.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author hujw
 * @description
 * @create 2021-09-30 21:33
 */

@Service
@Scope("singleton")//单例,默认
//@Scope("prototype")//多例
public class AlphaService {

    public AlphaService(){
        System.out.println("实例化AlphaService");
    }

    @PostConstruct
    public void init(){
        System.out.println("初始化AlphaService");
    }

    @PreDestroy
    public void destory(){
        System.out.println("销毁AlphaService");
    }
}
