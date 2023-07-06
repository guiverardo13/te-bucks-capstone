package com.techelevator.tebucks.model;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class UpdateTransferStatusDTO {

    String transferStatus;

    @AssertTrue
    private boolean validType () {
        return transferStatus.equals("Rejected") || transferStatus.equals("Approved");
    }

    public UpdateTransferStatusDTO(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }
}
