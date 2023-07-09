package com.techelevator.tebucks.model;

import javax.validation.constraints.AssertTrue;


public class UpdateTransferStatusDTO {

    private String  transferStatus;


    public String gettransferStatus() {
        return transferStatus;
    }

    public void settransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }
}
