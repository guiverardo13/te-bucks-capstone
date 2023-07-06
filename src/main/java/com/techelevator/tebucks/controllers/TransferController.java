package com.techelevator.tebucks.controllers;

import com.techelevator.tebucks.dao.JdbcTransferDao;
import com.techelevator.tebucks.dao.JdbcUserDao;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.TransferDTO;
import com.techelevator.tebucks.model.UpdateTransferStatusDTO;
import com.techelevator.tebucks.model.User;
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


    @RequestMapping(path = "/account/transfers", method = RequestMethod.GET)
    public List<Transfer> getTransfersById(Principal principal){
        String username = principal.getName();
        User user = userDao.getUserByUsername(username);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if(user.getUserName() != username){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot perform another user's transactions");
        }
        return transferDao.getTransfersByUserId(user.getUserId());
    }
    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
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
        User userLoggedIn = userDao.getUserByUsername(username);

        boolean validTransferCreation = userLoggedIn.getUserId() == transferDTO.getUserFrom() || userLoggedIn.getUserId() == transferDTO.getUserTo();

        if(!validTransferCreation){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized!");
        }

        return transferDao.createTransfer(transferDTO);
    }
    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(path = "/transfers/{id}/status", method = RequestMethod.PUT)
    public Transfer updateTransfer(@RequestBody @Valid UpdateTransferStatusDTO updateDTO, @PathVariable int transferId, Principal principal){
        String username = principal.getName();
        User userLoggedIn = userDao.getUserByUsername(username);
        Transfer transferToUpdate = getTransferById(transferId);

        if(transferToUpdate.getFromUserId() != userLoggedIn.getUserId()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized!");
        }
        return transferDao.updateTransferStatus(transferToUpdate, updateDTO.getTransferStatus());
    }


}