package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer getTransferByTransferId(int transferId);

    List<Transfer> getTransfersByUserId(int userId);

    // List<Transfer> get TransfersByStatus(String transferStatus);  ??



}
