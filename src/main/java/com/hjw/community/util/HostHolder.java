package com.hjw.community.util;

import com.hjw.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author hujw
 * @description
 * @create 2021-10-13 22:22
 */

/**
 * 持有用户信息，用于代替session对象
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }
    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
