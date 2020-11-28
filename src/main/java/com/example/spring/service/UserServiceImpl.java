package com.example.spring.service;

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
