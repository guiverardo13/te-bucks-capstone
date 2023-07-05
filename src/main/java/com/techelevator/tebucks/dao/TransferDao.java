package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getTransfers();

    Transfer getTransfersById(int transferId);

    // TransferStatusUpdateDto

    // 	NewTransferDto

}
