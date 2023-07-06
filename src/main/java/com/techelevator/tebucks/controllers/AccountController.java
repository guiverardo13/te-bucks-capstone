package com.techelevator.tebucks.controllers;

import com.techelevator.tebucks.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/api")
@RestController
public class AccountController {

    public Account getAccountById(@PathVariable int userId){

    }


}

