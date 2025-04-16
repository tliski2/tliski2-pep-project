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
     * @return message with the given id if found, null if not
     * @throws SQLException
     */
    public Message getMessageById(int message_id) throws SQLException {
        return messageDAO.getMessageById(message_id);
    }

    public List<Message> getMessagesByUser(int account_id) throws SQLException {
        return messageDAO.getMessagesByUser(account_id);
    }

    /**
     * Verfies correct message text and updates message if it exists
     * 
     * @param message_id
     * @param updated_text
     * @return updated message
     * @throws SQLException
     */
    public Message updateMessageById(int message_id, String updated_text) throws SQLException {
        if(updated_text.isBlank()) {
            throw new InputException("Message can't be blank.");
        }
        if(updated_text.length() > 255) {
            throw new  InputException("Message can't exceed 255 characters.");
        }
        if(messageDAO.getMessageById(message_id) == null) {
            throw new InputException("The message you are trying to update does not exist.");
        }

        return messageDAO.updateMessageById(message_id, updated_text);
    }

    /**
     * Deletes a specific message by id using messageDAO
     * 
     * @param message_id
     * @return deleted message if it existed, null if not
     * @throws SQLException
     */
    public Message deleteMessageById(int message_id) throws SQLException {
        Message message = messageDAO.getMessageById(message_id);
        if(message != null) {
            messageDAO.deleteMessageById(message_id);
        }
        return message;
    }

    
}
