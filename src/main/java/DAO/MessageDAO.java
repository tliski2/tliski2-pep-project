package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Message DAO class to handle the user message data from the front-end and interact with the database
 */
public class MessageDAO {

    /**
     * Retrieves all messages from the database
     * 
     * @return List of all messages
     * @throws SQLException
     */
    public List<Message> getAllMessages() throws SQLException{
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message";
        List<Message> messages = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                                            rs.getInt("posted_by"),
                                            rs.getString("message_text"),
                                            rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
            return messages;
        }
    }

    /**
     * Attempts to insert new message into the database
     * 
     * @param message to be inserted
     * @return the newly inserted message or null if unsuccessful
     * @throws SQLException
     */
    public Message insertMessage(Message message) throws SQLException{
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                    return new Message((int) rs.getLong(1), 
                                    message.getPosted_by(), 
                                    message.getMessage_text(), 
                                    message.getTime_posted_epoch());
                }
            }
        }
        return null;
    }


    
}
