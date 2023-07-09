package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.ErrorLog;
import com.techelevator.tebucks.model.TxLogDTO;

import java.util.List;

public interface ErrorLogDao {


    List<ErrorLog> listOfLoggerExceptions();

    ErrorLog getLoggerExceptionByExceptionId(int id);

    ErrorLog createLoggerExceptionLog (TxLogDTO txLogDTO);
}
