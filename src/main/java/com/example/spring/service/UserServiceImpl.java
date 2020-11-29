package com.example.spring.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

/**
 * @Component注解将类交给Spring管理，默认名称为首字母小写的类名
 * 使用@Conponent("UserService")修改名称
 * 该注解有三个衍生注解：
 * @Controller：注解Controller层的类
 * @Service：注解Service层的类
 * @Repository：注解Dao层的类
 */
@Component
public class UserServiceImpl implements UserService{
    @Override
    public void addUser() {
        System.out.println("add User...");
    }

    @Override
    public void deleteUser() {
        System.out.println("delete User...");
    }

    @Override
    public void updateUser() {
        System.out.println("update User...");
    }

    @Override
    public void searchUser() {
        System.out.println("search User...");
    }
}
