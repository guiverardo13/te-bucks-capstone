package com.techelevator.tebucks.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.techelevator.tebucks.security.model.User;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

public class Transfer {

    @NotNull
    private int transferId;
    @NotNull
    private String transferType;
    @NotNull
    @JsonProperty("userFrom")
    @JsonAlias("userFrom")
    private User userFrom;
    @NotNull
    @JsonProperty("userTo")
    @JsonAlias("userTo")
    private User userTo;
    @NotNull
    private double amount;
    @NotNull
    private String transferStatus;

    public int getFromUserId () {
        if ( userFrom == null) {
            return 0;
        } else {
            return userFrom.getId();
        }

    }

    public int getToUserId () {
        if (userTo == null) {
            return 0;
        } else {
            return userTo.getId();
        }
    }

    @AssertTrue
    private boolean validType(){
        return transferType.equals("Send") || transferType.equals("Request");
    }

    @AssertTrue
    private boolean validTransfer(){
        return userFrom.getId() != userTo.getId();
    }
    @AssertTrue
    private boolean validStatus(){
        return transferStatus.equals("Pending") || transferStatus.equals("Approved") || transferStatus.equals("Rejected");
    }
    public Transfer(){

    }
    public Transfer(int transferId, String transferType, User userFrom, User userTo, double transferAmount, String transferStatus) {
        this.transferId = transferId;
        this.transferType = transferType;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.amount = transferAmount;
        this.transferStatus = transferStatus;
    }

    public int getTransferId() {
        return transferId;
    }

    public String getTransferType() {
        return transferType;
    }

    public User getFromUser() {
        return userFrom;
    }

    public User getToUser() {
        return userTo;
    }

    public double getAmount() {
        return amount;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public void setFromUser(User userFrom) {
        this.userFrom = userFrom;
    }

    public void setToUser(User userTo) {
        this.userTo= userTo;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }
}
