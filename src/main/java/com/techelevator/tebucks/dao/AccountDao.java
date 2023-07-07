package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.Users;

public interface AccountDao {

    Account getAccount(int userId);



    double getBalance(int userId);

    //Added updateBalances method
    Account updateBalances(Transfer transferToUpdate);
}
