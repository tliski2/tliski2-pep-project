package Controller;

import Model.*;
import Service.*;
import Exception.InputException;
import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountCreationHandler);
        app.post("/login", this::postAccountLoginHandler);

        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);

        app.exception(SQLException.class, this::databaseExceptionHandler);
        app.exception(InputException.class, this::invalidInputHandler);
        app.exception(Exception.class, this::genericExceptionHandler);

        return app;
    }

    /**
     * Handler to post a new account.
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     * @throws SQLException
     */
    private void postAccountCreationHandler(Context ctx) throws JsonProcessingException, SQLException{
        ObjectMapper om = new ObjectMapper();
        Account accountToAdd = om.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(accountToAdd);
        ctx.status(200).json(addedAccount);
    }

    /**
     * Handler to login account
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     * @throws SQLException
     */
    private void postAccountLoginHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper om = new ObjectMapper();
        Account accountToLogin = om.readValue(ctx.body(), Account.class);
        Account loggedIn = accountService.getAccount(accountToLogin);
        if(loggedIn != null) {
            ctx.status(200).json(loggedIn);
        }
        else {
            ctx.status(401);
        }
    }

    /**
     * Handler to post a new message
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     * @throws SQLException
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper om = new ObjectMapper();
        Message messageToAdd = om.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(messageToAdd);
        ctx.status(200).json(addedMessage);
    }

    /**
     * Handler to retrieve all messages
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws SQLException
     */
    private void getAllMessagesHandler(Context ctx) throws SQLException {
        List<Message> messages = messageService.getAllMessages();
        ctx.status(200).json(messages);
    }

    /**
     * Handler to retrieve a specific message based on id
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws SQLException
     */
    private void getMessageByIdHandler(Context ctx) throws SQLException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        if(message != null) {
            ctx.status(200).json(message);
        }
        else{
            ctx.status(200);
        }
    }

    /**
     * Handler to delete a specific message based on id
     * 
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws SQLException
     */
    private void deleteMessageByIdHandler(Context ctx) throws SQLException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessageById(message_id);
        if(message != null) {
            ctx.status(200).json(message);
        }
        else {
            ctx.status(200);
        }
    }

    /**
     * Handler for database exceptions
     * 
     * @param e the SQLException thrown by the DAO/Service layer
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    private void databaseExceptionHandler(SQLException e, Context ctx) {
        ctx.status(500).result("Error with database.");
        e.printStackTrace();
    }
    
    /**
     * Handler for user input exceptions
     * 
     * @param e the InputException thrown by the service layer for invalid user input
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    private void invalidInputHandler(InputException e, Context ctx) {
        ctx.status(400);
        System.out.println(e.getMessage());
    }

    /**
     * Handler for all other exceptions
     * 
     * @param e the Exception thrown
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    private void genericExceptionHandler(Exception e, Context ctx) {
        ctx.result("Error occured: " + e.getMessage());
    }
}