package Service;

import Model.Message;
import DAO.*;
import Exception.InputException;
import java.sql.SQLException;
import java.util.List;

public class MessageService {
    MessageDAO messageDAO;
    
    public MessageService() {
        this.messageDAO = new MessageDAO();
    }
    
    /**
     * Verifies correct message attributes and calls MessageDAO to insert new message
     * 
     * @param message a Message object
     * @return created message if successful, null if not
     * @throws SQLException
     */
    public Message addMessage(Message message) throws SQLException {
        AccountDAO accountDAO = new AccountDAO();
        if(message.getMessage_text().isBlank()) {
            throw new InputException("Message can't be blank.");
        }
        if(message.getMessage_text().length() > 255) {
            throw new InputException("Message can't exceed 255 characters");
        }
        if(!accountDAO.accountExists(message.getPosted_by())) {
            throw new InputException("Invalid user.");
        }
        return messageDAO.insertMessage(message);
    }

    /**
     * Retrieves all messages from the database using messageDAO
     * 
     * @return List of all messages
     * @throws SQLException
     */
    public List<Message> getAllMessages() throws SQLException{
        return messageDAO.getAllMessages();
    }

    /**
     * Retrieves a specific message by id using messageDAO
     * 
     * @param message_id
     * @return message with the given id if found
     * @throws SQLException
     */
    public Message getMessageById(int message_id) throws SQLException {
        return messageDAO.getMessageById(message_id);
    }

    
}
