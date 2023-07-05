package com.techelevator.tebucks.model;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

public class Transfer {

    @NotNull
    private int transferId;
    @NotNull
    private String transferType;
    @NotNull
    private int fromUserId;
    @NotNull
    private int toUserId;
    @NotNull
    private double transferAmount;
    @NotNull
    private String status;

    @AssertTrue
    private boolean validType(){
        return transferType.equals("Send") || transferType.equals("Request");
    }

    @AssertTrue
    private boolean validTransfer(){
        return fromUserId != toUserId;
    }

    @AssertTrue
    private boolean sufficientFunds(){
        return transferAmount <= getBalance(fromUserId) && transferAmount > 0;
    }
    @AssertTrue
    private boolean validStatus(){
        return status.equals("Pending") || status.equals("Approved") || status.equals("Rejected");
    }
    public Transfer(){

    }
    public Transfer(int transferId, String transferType, int fromUserId, int toUserId, double transferAmount, String status) {
        this.transferId = transferId;
        this.transferType = transferType;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.transferAmount = transferAmount;
        this.status = status;
    }

    public int getTransferId() {
        return transferId;
    }

    public String getTransferType() {
        return transferType;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public double getTransferAmount() {
        return transferAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public void setTransferAmount(double transferAmount) {
        this.transferAmount = transferAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
