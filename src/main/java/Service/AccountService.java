package Service;

import Model.Account;
import DAO.AccountDAO;

/**
 * Service layer class to handle the business logic of user interactions (login/register) between the web layer (AccountController) and the data persistence layer (AccountDAO)
 */
public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public Account addAccount(Account account) {
        
    }
}
