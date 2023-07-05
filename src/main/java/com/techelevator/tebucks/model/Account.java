package com.techelevator.tebucks.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class Account {

    @NotNull
    private int userId;
    @NotNull
    @Positive
    private double balance = 1000;

    public Account(){

    }
    public Account(int userId, double balance) {
        this.userId = userId;
        this.balance = balance;
    }
    public int getUserId() {
        return userId;
    }
    public double getBalance() {
        return balance;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
