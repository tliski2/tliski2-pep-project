package Service;

import Model.Account;
import DAO.AccountDAO;
import Exception.InputException;
import java.sql.SQLException;

import org.eclipse.jetty.http.HttpTester.Input;

/**
 * Service layer class to handle the business logic of user interactions (login/register) between the web layer (AccountController) and the data persistence layer (AccountDAO)
 */
public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    /**
     * Verifies correct user input and calls the AccountDAO to insert new account
     * 
     * @param account an Account object
     * @return created account if successful, null if not
     */
    public Account addAccount(Account account) throws SQLException {
        if(account.getUsername().isBlank()) {
            throw new InputException("Username must have at least one character.");
        }
        if (account.getPassword().length() < 4) {
            throw new InputException("Password must be 4 or more characters long.");
        }
        if (accountDAO.accountExists(account.getUsername())) {
            throw new InputException("Username already exists.");
        }
        return accountDAO.insertAccount(account);
    }

    /**
     * Verifies account is valid and calls the AccountDAO to retrieve existing account
     * 
     * @param account information given by the client to "log in"
     * @return existing account if found, null if not
     * @throws SQLException
     */
    public Account getAccount(Account account) throws SQLException {
        return accountDAO.getAccount(account);
    }
}
