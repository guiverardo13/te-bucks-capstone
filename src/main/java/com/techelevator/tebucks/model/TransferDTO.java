package com.techelevator.tebucks.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class TransferDTO {

    @NotNull
    int userFrom;
    @NotNull
    int userTo;
    @Positive
    double amount;

    String transferType;

    @AssertTrue
    private boolean validType () {
        return transferType.equals("Send") || transferType.equals("Request");
    }

    @AssertTrue
    private boolean validTransfer () {
        return userFrom != userTo;
    }

    public TransferDTO(int userFrom, int userTo, double amount, String transferType) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.amount = amount;
        this.transferType = transferType;
    }

    public int getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(int userFrom) {
        this.userFrom = userFrom;
    }

    public int getUserTo() {
        return userTo;
    }

    public void setUserTo(int userTo) {
        this.userTo = userTo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }
}
