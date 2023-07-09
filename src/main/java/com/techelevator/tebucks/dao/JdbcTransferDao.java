package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.exception.DaoException;
import com.techelevator.tebucks.model.*;
import com.techelevator.tebucks.security.dao.JdbcUserDao;
import com.techelevator.tebucks.security.model.LoginDto;
import com.techelevator.tebucks.security.model.User;
import com.techelevator.tebucks.services.AuthenticationService;
import com.techelevator.tebucks.services.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

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
    @Autowired
    private LoggerService loggerService;
    @Autowired
    private AuthenticationService authenticationService;




    @Override
    public Transfer getTransferByTransferId(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type, from_user_id as from_user, to_user_id as to_user, amount, transfer_status FROM transfer " +
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
    public List <Transfer> getTransfers(int userId) {
        List <Transfer> transferList = new ArrayList<>();
        String sql = "Select transfer_id, transfer_type, from_users.user_id as from_user, to_users.user_id as to_user, amount, transfer_status\n" +
                "from transfer\n" +
                "join users as from_users on from_user_id = from_users.user_id\n" +
                "join users as to_users on to_user_id = to_users.user_id\n" +
                "order by transfer_id;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

            while (results.next()) {

                if (userId == results.getInt("from_user") || userId == results.getInt("to_user")) {
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


        String sql = "Update transfer set transfer_status = ? " +
                "where transfer_id = ?;";

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

        if (updatedTransfer.getTransferStatus().equals("Approved")) {

            if (transferToUpdate.getAmount() >= 1_000) {
                TxLogDTO txLogDTO = new TxLogDTO();
                txLogDTO.setUsernameFrom(updatedTransfer.getUserFrom().getUsername());
                txLogDTO.setUsernameTo(updatedTransfer.getUserTo().getUsername());
                txLogDTO.setAmount(updatedTransfer.getAmount());
                txLogDTO.setDescription("Transfer ID: " + Integer.toString(updatedTransfer.getTransferId()) + "; Status: " + updatedTransfer.getTransferStatus());
                loggerService.logTransaction(txLogDTO);

            }

            accountDao.updateBalances(transferToUpdate);
        }


        return updatedTransfer;
    }


    @Override
    public Transfer createTransfer (TransferDTO newTransferDTO) {

        User fromUser = userDao.getUserById(newTransferDTO.getUserFrom());
        User toUser = userDao.getUserById(newTransferDTO.getUserTo());

        Transfer newTransfer = new Transfer();
        newTransfer.setTransferType(newTransferDTO.getTransferType());
        newTransfer.setFromUser(fromUser);
        newTransfer.setToUser(toUser);
        newTransfer.setAmount(newTransferDTO.getAmount());

        TxLogDTO txLogDTO = new TxLogDTO();
        txLogDTO.setUsernameFrom(newTransfer.getUserFrom().getUsername());
        txLogDTO.setUsernameTo(newTransfer.getUserTo().getUsername());
        txLogDTO.setAmount(newTransfer.getAmount());


        String sqlBalanceCheck = "select balance from account where user_id = ?;";
        String sql = "Insert into transfer (transfer_type, from_user_id, to_user_id, " +
                "amount, transfer_status) " +
                "values (?, ?, ?, ?, ?) returning transfer_id;";


        try {
            Double balance = jdbcTemplate.queryForObject(sqlBalanceCheck, double.class, newTransferDTO.getUserFrom());
            boolean sufficientFunds = balance >= newTransferDTO.getAmount();


             if (newTransferDTO.getTransferType().equals("Send")) {

                 if (!sufficientFunds) {
                     newTransfer.setTransferStatus("Rejected");
                     txLogDTO.setDescription("Transfer ID: " + Integer.toString(newTransfer.getTransferId()) + "; Status: " + newTransfer.getTransferStatus());
                     loggerService.logTransaction(txLogDTO);

                     throw new DaoException("Insufficient Funds.");


                 } else {
                     newTransfer.setTransferStatus("Approved");

                     if (newTransfer.getAmount() >= 1_000) {
                         txLogDTO.setDescription("Transfer ID: " + Integer.toString(newTransfer.getTransferId()) + "; Status: " + newTransfer.getTransferStatus());
                         loggerService.logTransaction(txLogDTO);
                     }

                     accountDao.updateBalances(newTransfer);
                 }
             } else if (newTransferDTO.getTransferType().equals("Request")) {
                newTransfer.setTransferStatus("Pending");
            }


            int newTransferId = jdbcTemplate.queryForObject(sql, int.class,  newTransfer.getTransferType(), newTransfer.getFromUserId(),
                    newTransfer.getToUserId(), newTransfer.getAmount(), newTransfer.getTransferStatus());

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
        transfer.setFromUser(userDao.getUserById(rs.getInt("from_user")));
        transfer.setToUser(userDao.getUserById(rs.getInt("to_user")));
        transfer.setAmount(rs.getDouble("amount"));
        transfer.setTransferStatus(rs.getString("transfer_status"));

        return transfer;
    }


}
