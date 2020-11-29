package com.example.spring.pojo;

import com.example.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class User {
    //使用@Value注入普通属性的值
    @Value("Liuwei")
    private String name;
    @Value("23")
    private int age;
    //使用@Autowired注解对象类型的属性
    //@Autowired按照类型注入
    //想要按照名称注入，需要再使用@Qualifier
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;
    //可以使用@Resource注解按照名称进行属性的注入
    @Resource(name = "userServiceImpl")
    private UserService userService1;

    /*public User(String name, int age) {
        this.name = name;
        this.age = age;
    }*/

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UserService getUserService() {
        return userService1;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
