package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.exception.DaoException;
import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.Users;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcAccountDao implements AccountDao{
    private final JdbcTemplate jdbcTemplate;



    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccount(int userId) {
        Account account = new Account();
        String sql = "SELECT user_id, balance FROM account where user_id =?;";

        try {
           SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);

           if (rowSet.next()) {
               account = mapRowToAccount(rowSet);
           }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return account;

    }

    @Override
    public double getBalance(int userId){
        Double balance;
        String sql = "select balance from account where user_id = ?;";

        try {
            balance = jdbcTemplate.queryForObject(sql, double.class, userId);

            if(balance == null) {
                throw new DaoException("Unable to get balance");
            }
            return balance;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    //Added updateBalances method
    @Override
    public Account updateBalances(Transfer transferToUpdate) {
        double amountToUpdate = transferToUpdate.getTransferAmount();
        int toId = transferToUpdate.getToUserId();
        int fromId = transferToUpdate.getFromUserId();


        String sql = "update account set balance = ? where user_id = ?;";

        try {
            jdbcTemplate.update(sql, getBalance(toId) + amountToUpdate, toId);
            jdbcTemplate.update(sql, getBalance(fromId) - amountToUpdate, fromId);

            return getAccount(fromId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Cannot update Balances", e);
        }
    }

    private Account mapRowToAccount (SqlRowSet rowSet) {
        Account newAccount = new Account();
        newAccount.setUserId(rowSet.getInt("user_id"));
        newAccount.setBalance(rowSet.getDouble("balance"));
        return newAccount;

    }
}
