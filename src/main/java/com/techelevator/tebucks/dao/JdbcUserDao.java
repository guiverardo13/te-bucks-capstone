package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.exception.DaoException;
import com.techelevator.tebucks.model.User;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao implements UserDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash, first_name, last_name, email, role FROM users";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                User user = mapRowToUser(results);
                userList.add(user);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return userList;
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUserName(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        return user;
    }
}

