package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.TransferDTO;

import java.util.List;

public interface TransferDao {

    Transfer getTransferByTransferId(int transferId);

    List<Transfer> getTransfersByUserId(int userId);

    Transfer updateTransferStatus (Transfer transferToUpdate, String status);

    Transfer createTransfer (TransferDTO newTransferDTO);
}