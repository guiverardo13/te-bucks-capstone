
/*
package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.exception.DaoException;
import com.techelevator.tebucks.model.Users;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUsersDao implements UsersDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUsersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Users> getUsers() {
        List<Users> usersList = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash, first_name, last_name, email, role " +
                     "FROM users " +
                     "ORDER BY first_name;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Users users = mapRowToUser(results);
                usersList.add(users);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return usersList;
    }
    @Override
    public Users getUserByUsername(String username) {
        String sql = "SELECT user_id, username, password_hash, first_name, last_name, email, role " +
                "FROM users " +
                "WHERE username = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, Users.class, username);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }

    @Override
    public Users getUserByUserId(int userId){
        String sql = "SELECT user_id, username, password_hash, first_name, last_name, email, role " +
                     "FROM users " +
                     "WHERE user_id = ?;";

        try{
            return jdbcTemplate.queryForObject(sql, Users.class, userId);

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }



    private Users mapRowToUser(SqlRowSet rs) {
        Users users = new Users();
        users.setUserId(rs.getInt("user_id"));
        users.setUserName(rs.getString("username"));
        users.setPasswordHash(rs.getString("password_hash"));
        users.setFirstName(rs.getString("first_name"));
        users.setLastName(rs.getString("last_name"));
        users.setEmail(rs.getString("email"));
        users.setRole(rs.getString("role"));
        return users;
    }
}

*/