package com.techelevator.tebucks.security.dao;

import com.techelevator.tebucks.security.model.RegisterUserDto;
import com.techelevator.tebucks.security.model.User;

import java.security.Principal;
import java.util.List;

public interface UserDao {


    List<User> getUsers(Principal principal);

    User getUserByUsername(String username);

    User createUser(RegisterUserDto user);

    //void setInitialBalance(int userId);
}
