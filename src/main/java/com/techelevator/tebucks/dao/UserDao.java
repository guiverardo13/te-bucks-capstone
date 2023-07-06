package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.User;

import java.util.List;

public interface UserDao {

    List<User> getUsers();
    User getUserByUsername(String username);
    User getUserByUserId(int userId);

}
