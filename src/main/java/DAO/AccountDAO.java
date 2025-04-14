package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Account DAO class to handle the user login/registration data from the front-end and interact with the database
 */

public class AccountDAO {

    /*
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
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            int rows = ps.executeUpdate();
            return rows > 0 ? account : null;
        }

    }
    
}
