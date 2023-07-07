package com.techelevator.tebucks.model;

import javax.validation.constraints.NotNull;

public class Users {

    @NotNull
    private int userId;
    @NotNull
    private String userName;
    @NotNull
    private String passwordHash;
    private Account userAccount;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    public String fullName(){
        return firstName + " " + lastName;
    }
    public Users(){

    }
    public Users(int userId, String userName, String passwordHash, Account userAccount, String firstName, String lastName, String email, String role) {
        this.userId = userId;
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.userAccount = userAccount;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }
    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Account getUserAccount() {
        return userAccount;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setUserAccount(Account userAccount) {
        this.userAccount = userAccount;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
