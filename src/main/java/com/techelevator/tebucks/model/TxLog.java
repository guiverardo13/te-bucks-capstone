package com.techelevator.tebucks.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class TxLog {

    @NotNull
    private String description;
    @NotNull
    @JsonProperty("username_from")
    private String usernameFrom;
    @NotNull
    @JsonProperty("username_to")
    private String usernameTo;
    @NotNull
    private double amount;
    @NotNull
    @JsonProperty("log_id")
    private int logId;
    @NotNull
    private String createdDate;

    public TxLog(String description, String usernameFrom, String usernameTo, double amount, int logId, String createdDate) {
        this.description = description;
        this.usernameFrom = usernameFrom;
        this.usernameTo = usernameTo;
        this.amount = amount;
        this.logId = logId;
        this.createdDate = createdDate;
    }

    public TxLog () {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }

    public String getUsernameTo() {
        return usernameTo;
    }

    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
