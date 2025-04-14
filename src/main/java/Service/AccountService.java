package Service;

import Model.Account;

import java.sql.SQLException;

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
        try {
            if(!account.getUsername().isBlank() && account.getPassword().length() >= 4 && !accountDAO.accountExists(account.getUsername())) {
                return accountDAO.insertAccount(account);
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
