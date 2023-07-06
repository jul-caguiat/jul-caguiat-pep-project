package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAOImpl implements AccountDAO {

    // connect to database when AccountDAOImpl class is instantiated.
    private Connection conn;
    public AccountDAOImpl() {
        conn = ConnectionUtil.getConnection();
    }

    // DAO method implementation to insert a new account.
    @Override
    public Account insertNewAccount(Account newAccount) {
        
        try {

            // SQL query to insert a new account into 'account' table if 'username' does not exists.
            String sql = "INSERT INTO account (username, password)" +
                    "SELECT ?, ? " +
                    "WHERE " +
                    "NOT EXISTS " +
                    "(SELECT username FROM account WHERE username = ?);";

            // PreparedStatement to set variables and execute the sql statements.
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newAccount.getUsername());
            ps.setString(2, newAccount.getPassword());
            ps.setString(3, newAccount.getUsername());

            // executeUpdate() to return numbers of rows affected in the database.
            ps.executeUpdate();

            // SQL query to retrieve 'account_id' from 'account' table for a given 'username'.
            sql = "SELECT account_id FROM account WHERE username = ?";
            PreparedStatement ps1 = conn.prepareStatement(sql);
            ps1.setString(1, newAccount.getUsername());

            // executeQuery() to return a result set from the database.
            ResultSet rs = ps1.executeQuery();

            // if 'rs' result set is empty (false) return 'null', otherwise set 'account_id' 
            // in the 'newAccount' object from 'rs' value then return 'newAccount'.
            if (!rs.next()) {
                return null;
            } else {
                newAccount.setAccount_id(rs.getInt("account_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newAccount;
    }

    // DAO method implementation to verify user log-in.
    @Override
    public Account verifyAccountLogin(Account account) {

        try {

            // SQL query to retrieve a record for given 'username' and 'password'.
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";

            // PreparedStatement to set the variables and execute the sql statements.
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            // executeQuery() to return a result set from the database.
            ResultSet rs = ps.executeQuery();

            // if 'rs' is empty (false) return 'null', otherwise set 'account_id' 
            // in the 'account' object from 'rs' value then return 'account'.
            if (!rs.next()) {
                return null;
            } else {
                account.setAccount_id(rs.getInt("account_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
   
    // DAO method implementation to retrieve a particular account by account id.
    @Override
    public Account getAccountByID(int accountID) {

        // instantiate Account class to be returned.
        Account targetAccount = new Account();

        try {

            // SQL query to retrieve a record for a given 'account_id'.
            String sql = "SELECT * FROM account WHERE account_id = ?";

            // PreparedStatement to set the varables and execute the sql statements.
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountID);

            // executeQuery() to return a result set from the database.
            ResultSet rs = ps.executeQuery();

            // if 'rs' is empty (false) return 'null', otherwise set the 'account_id', 
            // 'username' and 'password' in the 'targetAcct' object from 'rs' values then return 'targetAccount'.
            if (!rs.next()) {
                return null;
            } else {
                targetAccount.setAccount_id(rs.getInt("account_id"));
                targetAccount.setUsername(rs.getString("username"));
                targetAccount.setPassword(rs.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return targetAccount;
    }

    // DAO method implementation to check if username alredy exists.
    @Override
    public boolean checkIfUsernameExists(String name) {

        try {

            // SQL query to retrieve a record for a given 'username'.
            String sql = "SELECT username FROM account WHERE username = ?";

            // PreparedStatement to set the variables and execute the sql statements.
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);

            // executeQuery() to return a result set from the database.
            ResultSet rs = ps.executeQuery();

            // if 'rs' is empty return 'false', otherwise return 'true' (username already exists).
            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
             e.printStackTrace();
        }
        return true;
    }
     
}
