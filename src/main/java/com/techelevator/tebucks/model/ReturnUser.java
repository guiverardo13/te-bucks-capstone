package com.techelevator.tebucks.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ReturnUser {
    @NotNull
    private String username;
    @JsonProperty("api_key")
    @NotNull
    private int apiKey;
    @NotNull
    private int userId;
    @NotNull
    private String token;

    public ReturnUser() {

    }


    public ReturnUser(String username, int apiKey, int userId, String token) {
        this.username = username;
        this.apiKey = apiKey;
        this.userId = userId;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getApiKey() {
        return apiKey;
    }

    public void setApiKey(int apiKey) {
        this.apiKey = apiKey;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}



