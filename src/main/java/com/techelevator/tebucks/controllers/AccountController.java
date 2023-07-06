package com.techelevator.tebucks.controllers;

import com.techelevator.tebucks.dao.JdbcAccountDao;
import com.techelevator.tebucks.dao.JdbcUserDao;
import com.techelevator.tebucks.exception.DaoException;
import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/api")
@RestController
public class AccountController {
    @Autowired
    private JdbcUserDao userDao;
    @Autowired
    private JdbcAccountDao accountDao;

    @RequestMapping(path = "/account/balance", method = RequestMethod.GET)
    public Account getAccountById(Principal principal){
        String username = principal.getName();
        User userLoggedIn = userDao.getUserByUsername(username);

        return accountDao.getAccount(userLoggedIn.getUserId());
    }


}

