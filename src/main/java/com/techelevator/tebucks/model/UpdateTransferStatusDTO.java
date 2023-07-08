package com.techelevator.tebucks.model;

import javax.validation.constraints.AssertTrue;


public class UpdateTransferStatusDTO {

    private String  transferStatus;

    //@AssertTrue
    //private boolean validType () {
    //    return transferStatus.equals("Approved") || transferStatus.equals("Rejected");
    //}

    //public UpdateTransferStatusDTO(String transferStatus) {
    //    this.transferStatus = transferStatus;
    //}

    public String gettransferStatus() {
        return transferStatus;
    }

    public void settransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }
}
