package com.techelevator.tebucks.controllers;



import com.techelevator.tebucks.security.dao.JdbcUserDao;
import com.techelevator.tebucks.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api")
public class UsersController {

    @Autowired
    private JdbcUserDao userDao;



    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> getAllUsers(Principal principal){
        return userDao.getUsers(principal);
    }
}
