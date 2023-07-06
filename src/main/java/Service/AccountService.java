package Service;

import DAO.AccountDAOImpl;
import Model.Account;

public class AccountService {

    // AccountDAOImpl object variable declaration 
    private AccountDAOImpl acctDAOImpl;

    // AccountService class constructor for class intantiation (no parameter).
    public AccountService(){
        acctDAOImpl = new AccountDAOImpl();
    }

    // AccountService class constructor (with parameter)
    public AccountService(AccountDAOImpl acctDAOImpl){
        this.acctDAOImpl = acctDAOImpl;
    }

    // Service method to add a new account (with input validation).
    public Account addAccount(Account account) {

        // check if account already exists using acctDAOIMPL's 'checkIfUsernameExists' method.
        boolean acctExists = acctDAOImpl.checkIfUsernameExists(account.getUsername());

        // if 'acctExists' is 'true', or 'username' is blank, or 'password' is less than 4 characters return 'null'.
        if (acctExists || (account.getUsername().isEmpty())
                || (account.getUsername().isBlank())
                || (account.getPassword().length() < 4)) {

            return null;
        }

        // call acctDAOImpl's 'insertNewAccount' method then return 
        // the added account object if successful, otherwise, result is 'null'.
        return acctDAOImpl.insertNewAccount(account);
    }

    // Service method to verify account log-in. 
    public Account verifyAccountLogin(Account acct) {

        // call acctDAOIMPL's 'verifyAccountLogin' method then return 
        // the verified account object if successful, otherwise, result is 'null'.
        return acctDAOImpl.verifyAccountLogin(acct);
    } 
    
}
