package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.exception.DaoException;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.TransferDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;


    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type, from_user_id, to_user_id, amount, transfer_status FROM transfer" +
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
    public List <Transfer> getTransfersByUserId(int userId) {
        List <Transfer> transferList = new ArrayList<>();
        String sql = "Select users.first_name as from_user, to_user.first_name as to_user, " +
                "amount, transfer_id, transfer_status " +
                "from transfer " +
                "join users as from_users on user_id = from_user_id " +
                "join users as to_users on user_id = to_user_id " +
                "where from_user_id = ? or to_user_id = ? " +
                "order by transfer_id;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()) {
                 transferList.add(mapRowToTransfer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferList;
    }

    @Override
    public Transfer updateTransferStatus (Transfer transferToUpdate, String status) {
        Transfer updatedTransfer;


        String sql = "Update transfer set transfer_status = ?" +
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

        return updatedTransfer;
    }

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

            Double balance = jdbcTemplate.queryForObject(sqlBalanceCheck, double.class, newTransferDTO.getUserFrom());

            boolean sufficientFunds = balance >= newTransferDTO.getAmount();

            if (!sufficientFunds && newTransferDTO.getTransferType().equals("Send")) {
                newTransfer.setStatus("Rejected");

            } else if (newTransferDTO.getTransferType().equals("Send")) {
                newTransfer.setStatus("Approved");

            } else if (newTransferDTO.getTransferType().equals("Request")) {
                newTransfer.setStatus("Pending");
            }

           Integer newTransferId = jdbcTemplate.queryForObject(sql, int.class, newTransfer.getTransferType(), newTransfer.getFromUserId(),
                    newTransfer.getToUserId(), newTransfer.getTransferAmount(), newTransfer.getStatus());

           if (newTransferId == null) {
               throw new DaoException("Unable to create new Transfer");
           }

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
