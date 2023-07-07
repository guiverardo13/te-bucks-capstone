package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.exception.DaoException;
import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.TransferDTO;
import com.techelevator.tebucks.model.UpdateTransferStatusDTO;
import com.techelevator.tebucks.security.dao.JdbcUserDao;
import com.techelevator.tebucks.security.dao.UserDao;
import com.techelevator.tebucks.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcAccountDao accountDao;
    @Autowired
    private JdbcUserDao userDao;




    @Override
    public Transfer getTransferByTransferId(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type, from_user_id, to_user_id, amount, transfer_status FROM transfer " +
                     "WHERE transfer_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }

    @Override
    public List <Transfer> getTransfersByUserId(int userId, Principal principal) {
        List <Transfer> transferList = new ArrayList<>();
        String sql = "SELECT users.first_name AS from_user, to_user.first_name AS to_user, " +
                     "amount, transfer_id, transfer_status " +
                     "FROM transfer " +
                     "JOIN users as from_users on user_id = from_user_id " +
                     "JOIN users as to_users on user_id = to_user_id " +
                     "WHERE from_user_id = ? or to_user_id = ? " +
                     "ORDER BY transfer_id;";
        String sql = "Select transfer_id, from_users.first_name as from_user, to_users.first_name as to_user, amount, transfer_status\n" +
                "from transfer\n" +
                "join users as from_users on from_user_id = from_users.user_id\n" +
                "join users as to_users on to_user_id = to_users.user_id\n" +
                "order by transfer_id;";
        

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            String principalName = principal.getName();
            User user = userDao.getUserByUsername(principalName);
            while (results.next()) {

                if (user.getId() == results.getInt("from_user") || user.getId() == results.getInt("to_user")) {
                    transferList.add(mapRowToTransfer(results));
                }
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferList;
    }

    @Override
    public Transfer updateTransferStatus (Transfer transferToUpdate, String status) {
        Transfer updatedTransfer;


        String sql = "UPDATE transfer " +
                     "SET transfer_status = ? " +
                     "WHERE transfer_id = ?;";

        try {
            int rowsUpdated = jdbcTemplate.update(sql, status, transferToUpdate.getTransferId());

            if (rowsUpdated == 0) {
                throw new DaoException("Unable to update Transfer Status.");
            }
            updatedTransfer = getTransferByTransferId(transferToUpdate.getTransferId());

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        //add money / subtract money method
        accountDao.updateBalances(transferToUpdate);
        return updatedTransfer;
    }

    // added update Balances method
    @Override
    public Transfer createTransfer (TransferDTO newTransferDTO) {

        Transfer newTransfer = new Transfer();
        newTransfer.setTransferType(newTransferDTO.getTransferType());
        newTransfer.setFromUserId(newTransferDTO.getUserFrom());
        newTransfer.setToUserId(newTransferDTO.getUserTo());
        newTransfer.setTransferAmount(newTransferDTO.getAmount());

        String sqlBalanceCheck = "select balance from account where user_id = ?;";
        String sql = "Insert into transfer (transfer_type, from_user_id, to_user_id, " +
                "amount, transfer_status) " +
                "values (?, ?, ?, ?, ?) returning transfer_id;";


        try {


             if (newTransferDTO.getTransferType().equals("Send")) {
                Double balance = jdbcTemplate.queryForObject(sqlBalanceCheck, double.class, newTransferDTO.getUserFrom());
                boolean sufficientFunds = balance >= newTransferDTO.getAmount();
                 if (!sufficientFunds) {
                     newTransfer.setStatus("Rejected");

                 } else {
                     newTransfer.setStatus("Approved");
                     accountDao.updateBalances(newTransfer);
                 }


            } else if (newTransferDTO.getTransferType().equals("Request")) {
                newTransfer.setStatus("Pending");
            }


            int newTransferId = jdbcTemplate.queryForObject(sql, int.class,  newTransfer.getTransferType(), newTransfer.getFromUserId(),
                    newTransfer.getToUserId(), newTransfer.getTransferAmount(), newTransfer.getStatus());

            newTransfer.setTransferId(newTransferId);
            return getTransferByTransferId(newTransferId);

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Cannot connect to database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Unable to create new Transfer", e);
        }
    }


    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();

        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferType(rs.getString("transfer_type"));
        transfer.setFromUserId(rs.getInt("from_user_id"));
        transfer.setToUserId(rs.getInt("to_user_id"));
        transfer.setTransferAmount(rs.getDouble("amount"));
        transfer.setStatus(rs.getString("transfer_status"));

        return transfer;
    }


}
