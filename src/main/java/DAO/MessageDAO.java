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
    public List<Message> getAllMessages() throws SQLException {
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
     * Retrieves a specific message based on the id 
     *
     * @param message_id
     * @return Message if found, null if not
     * @throws SQLException
     */
    public Message getMessageById(int message_id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, message_id);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                                            rs.getInt("posted_by"),
                                            rs.getString("message_text"),
                                            rs.getLong("time_posted_epoch"));
                return message;
            }
            return null;
        }
    }

    /**
     * Retrieves all messages by the given account from the database
     * 
     * @param account_id
     * @return List of all messages tied to the given account_id
     * @throws SQLException
     */
    public List<Message> getMessagesByUser(int account_id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        List<Message> messages = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, account_id);

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

    /**
     * Attempts to update the message text for a message with the given id
     * 
     * @param message_id
     * @param updated_text
     * @return the updates message object if successful, null if not
     * @throws SQLException
     */
    public Message updateMessageById(int message_id, String updated_text) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, updated_text);
            ps.setInt(2, message_id);

            int updated_rows = ps.executeUpdate();
            if (updated_rows > 0) {
                return this.getMessageById(message_id);
            }
            return null;
        }
    }

    /**
     * Attempts to delete a specific message based on the id
     * 
     * @param message_id
     * @throws SQLException
     */
    public void deleteMessageById(int message_id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "DELETE FROM message WHERE message_id = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, message_id);
            ps.executeUpdate();
        }
    } 
}
