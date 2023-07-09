package com.techelevator.tebucks.model;

import javax.validation.constraints.NotNull;

public class TearsLoginUser {

    @NotNull
    private String username;
    @NotNull
    private String password;

    public TearsLoginUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public TearsLoginUser() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

