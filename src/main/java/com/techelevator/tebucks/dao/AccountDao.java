package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Account;

public interface AccountDao {

    Account getBalance(int userId);
}
