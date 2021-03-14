package com.bwee.webit.heos.model;

public class Account {

    private String username;
    private String password;

    public Account(String username){
        this.username = username;
    }

    public Account(String username, String password){
        this.username = username;
        this.password = password;
    }
}
