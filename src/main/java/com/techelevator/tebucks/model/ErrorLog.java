package com.techelevator.tebucks.model;

import javax.validation.constraints.NotNull;

public class ErrorLog {

    @NotNull
    private int errorId;
    @NotNull
    private int transferId;
    @NotNull
    private String date;
    @NotNull
    private String description;
    @NotNull
    private String fromUser;
    @NotNull
    private String toUser;
    @NotNull
    private double amount;

    public ErrorLog(int errorId, int transferId, String date, String description, String fromUser, String toUser, double amount) {
        this.errorId = errorId;
        this.transferId = transferId;
        this.date = date;
        this.description = description;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
    }

    public ErrorLog() {

    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
