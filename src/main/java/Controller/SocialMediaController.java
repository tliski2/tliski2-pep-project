package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;

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

    public SocialMediaController() {
        this.accountService = new AccountService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);

        return app;
    }

    /**
     * Handler to post a new account.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Account accountToAdd = om.readValue(ctx.body(), Account.class);

        Account addedAccount = accountService.addAccount(accountToAdd);
        if(addedAccount != null) {
            ctx.json(om.writeValueAsString(addedAccount));
        }
        else {
            ctx.status(400);
        }


    }


}