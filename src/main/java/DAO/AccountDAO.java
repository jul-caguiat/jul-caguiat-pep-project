package DAO;

import Model.Account;

public interface AccountDAO {

    // abstract methods to be implemented.
    public Account insertNewAccount(Account newAccount);
    public Account verifyAccountLogin(Account account);
    public Account getAccountByID(int id);
    public boolean checkIfUsernameExists(String name);
    
}
