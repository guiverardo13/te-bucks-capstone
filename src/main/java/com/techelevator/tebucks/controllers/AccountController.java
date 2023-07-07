package com.techelevator.tebucks.controllers;

import com.techelevator.tebucks.dao.JdbcAccountDao;

import com.techelevator.tebucks.model.Account;

import com.techelevator.tebucks.security.dao.JdbcUserDao;
import com.techelevator.tebucks.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        User usersLoggedIn = userDao.getUserByUsername(username);


        return accountDao.getAccount(userLoggedIn.getId());
    }
}

