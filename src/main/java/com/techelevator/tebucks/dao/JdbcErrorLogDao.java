package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.exception.DaoException;
import com.techelevator.tebucks.model.ErrorLog;
import com.techelevator.tebucks.model.TxLogDTO;
import com.techelevator.tebucks.services.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;




import java.time.LocalDateTime;

import java.util.ArrayList;

import java.util.List;

@Component
public class JdbcErrorLogDao implements ErrorLogDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;





    @Override
    public List<ErrorLog> listOfLoggerExceptions() {

        List<ErrorLog> listOfLoggerExceptions = new ArrayList<>();

        String sql = "Select * from logger_exceptions order by exception_id;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);

            while (rowSet.next()) {
                listOfLoggerExceptions.add(mapRowToErrorLog(rowSet));
            }
            return listOfLoggerExceptions;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Cannot connect to database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Cannot get list of exception logs.", e);
        }
    }

    @Override
    public ErrorLog getLoggerExceptionByExceptionId(int id) {
        ErrorLog newEntry = null;

        String sql = "Select * from logger_exceptions where exception_id = ?;";

        try {

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);

            if (rowSet.next()) {
                return mapRowToErrorLog(rowSet);

            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database",e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Unable to get Log.", e);
        }

        return newEntry;

    }

    @Override
    public ErrorLog createLoggerExceptionLog(TxLogDTO txLogDTO) {
        ErrorLog newLoggerException = new ErrorLog();

        String sql = "Insert into logger_exceptions (exception_date, description, " +
                "from_user, to_user, amount) values (?, ?, ?, ?, ?) returning exception_id;";

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd" ) ;
        //LocalDate localDate = LocalDate.parse ( LocalDate.now().toString(), formatter ) ;

        try {
            int newLogId = jdbcTemplate.queryForObject(sql, int.class, LocalDateTime.now().toString() , txLogDTO.getDescription(), txLogDTO.getUsernameFrom(), txLogDTO.getUsernameTo(), txLogDTO.getAmount());

            return getLoggerExceptionByExceptionId(newLogId);


        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Cannot connect to database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Cannot create new Exception Log", e);
        }

    }

    private ErrorLog mapRowToErrorLog (SqlRowSet rowSet) {
        ErrorLog newEntry = new ErrorLog();
        newEntry.setErrorId(rowSet.getInt("exception_id"));
        newEntry.setDate(rowSet.getString("exception_date").toString());
        newEntry.setDescription(rowSet.getString("description"));
        newEntry.setFromUser(rowSet.getString("from_user"));
        newEntry.setToUser(rowSet.getString("to_user"));
        newEntry.setAmount(rowSet.getDouble("amount"));
        return newEntry;

    }
}
