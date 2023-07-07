package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Users;

import java.security.Principal;
import java.util.List;

public interface UsersDao {

    List<Users> getUsers();



    Users getUserByUsername(String username);
    Users getUserByUserId(int userId);

}
