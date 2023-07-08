package com.techelevator.tebucks.controllers;

import com.techelevator.tebucks.dao.JdbcAccountDao;
import com.techelevator.tebucks.dao.JdbcTransferDao;

import com.techelevator.tebucks.exception.DaoException;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.TransferDTO;
import com.techelevator.tebucks.model.UpdateTransferStatusDTO;
import com.techelevator.tebucks.security.dao.JdbcUserDao;
import com.techelevator.tebucks.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api")
public class TransferController {

    @Autowired
    private JdbcTransferDao transferDao;
    @Autowired
    private JdbcUserDao userDao;
    @Autowired
    private JdbcAccountDao accountDao;


    @RequestMapping(path = "/account/transfers", method = RequestMethod.GET)
    public List<Transfer> getTransfers(Principal principal){
        String username = principal.getName();
        User users = userDao.getUserByUsername(username);

        if(users == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return transferDao.getTransfers(users.getId());
    }

    @RequestMapping(path = "/transfers/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int transferId){
        Transfer transfer = transferDao.getTransferByTransferId(transferId);

        if(transfer == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found");
        }
        return transfer;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfers", method = RequestMethod.POST)

    public Transfer createTransfer(@RequestBody @Valid TransferDTO transferDTO, Principal principal){
        String username = principal.getName();
        User usersLoggedIn = userDao.getUserByUsername(username);

        if (transferDTO.getAmount() <= 0){
            throw new DaoException("Unable to transfer the selected amount.");
        }

        if (transferDTO.getTransferType().equals("Request")) {
            transferDTO.setUserTo(usersLoggedIn.getId());
        } else {
            transferDTO.setUserFrom(usersLoggedIn.getId());
        }






        return transferDao.createTransfer(transferDTO);
    }


    //@ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(path = "/transfers/{id}/status", method = RequestMethod.PUT)
    public Transfer updateTransfer(@RequestBody  UpdateTransferStatusDTO updateDTO, @PathVariable int id, Principal principal){
        String username = principal.getName();
        User usersLoggedIn = userDao.getUserByUsername(username);
        Transfer transferToUpdate = getTransferById(id);

        double balance = accountDao.getBalance(transferToUpdate.getFromUserId());




        boolean sufficientFunds = balance >= transferToUpdate.getAmount();
        boolean isAuthorized = transferToUpdate.getFromUserId() == usersLoggedIn.getId();
        boolean isRequest = transferToUpdate.getTransferType().equals("Request");
        boolean isPending = transferToUpdate.getTransferStatus().endsWith("Pending");

        if(isAuthorized && isRequest && updateDTO.gettransferStatus().equals("Rejected")){
            return transferDao.updateTransferStatus(transferToUpdate,"Rejected");
        }


        if(!sufficientFunds || !isAuthorized || !isRequest || !isPending){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized!");
        }
        return transferDao.updateTransferStatus(transferToUpdate, updateDTO.gettransferStatus());
    }


}
