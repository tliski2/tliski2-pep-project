package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Account DAO class to handle the user login/registration data from the front-end and interact with the database
 */

public class AccountDAO {

    /**
     * Attempts to insert new user account into the database
     * 
     * @param account to be inserted
     * @return the newly inserted account or null if unsuccessful
     * @throws SQLException
     * 
     */
    public Account insertAccount(Account account) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                    int account_id = (int) rs.getLong(1);
                    return new Account(account_id, account.getUsername(), account.getPassword());
                }
            }

        }
        return null;

    }

    /**
     * Attempts to "log in" by checking database for the given account details
     * 
     * @param account information given by the client to "log in"
     * @return logged in account object if it exists
     * @throws SQLException
     */
    public Account getAccount(Account account) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Account loggedIn = new Account(rs.getInt("account_id"), 
                                                rs.getString("username"), 
                                                rs.getString("password"));
                return loggedIn;
            }
            return null;
        }
    }

    /**
     * Utility to check if an account already exists (by username) before registering a new one
     * 
     * @param username to check if it already exists
     * @return true if account exists, false if not
     * @throws SQLException
     */
    public boolean accountExists(String username) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        }

    }
    
}
