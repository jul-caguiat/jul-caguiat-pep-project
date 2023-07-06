package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class MessageDAOImpl implements MessageDAO {
    
    // connect to database when MessageDAOImpl class is instantiated.
    private Connection conn;
    public MessageDAOImpl() {
        conn = ConnectionUtil.getConnection();
    }

    // DAO method implementation to insert a new message.
    @Override
    public Message insertNewMessage(Message newMessage) {

        try {

            // SQL query to insert a new message into 'message' table if 'account_id' (posted_by) exists.
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch)" +
                    "SELECT ?, ?, ? " +
                    "WHERE " +
                    "EXISTS " +
                    "(SELECT account_id FROM account WHERE account_id = ?);";

            // PreparedStatement to set variables and execute the sql statements.
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, newMessage.getPosted_by());
            ps.setString(2, newMessage.getMessage_text());
            ps.setLong(3, newMessage.getTime_posted_epoch());
            ps.setInt(4, newMessage.getPosted_by());

            // executeUpdate() to return numbers of rows affected in the database.
            ps.executeUpdate();

            // SQL query to retrieve 'message_id' from the 'message' table for a given 'posted_by' value.
            String sql2 = "Select message_id FROM message WHERE message_text = ? AND posted_by = ?";

            PreparedStatement ps1 = conn.prepareStatement(sql2);
            ps1.setString(1, newMessage.getMessage_text());
            ps1.setInt(2, newMessage.getPosted_by());

            // executeQuery to return a result set from the database.
            ResultSet rs = ps1.executeQuery();

            // if 'rs' is empty (false) return 'null', 
            // otherwise set 'message_id' in the 'newMessage' object from 'rs' value then return 'newMessage'.
            if (!rs.next())
                return null;
            else
                newMessage.setMessage_id(rs.getInt("message_id"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newMessage;
    }

    // DAO method implementation to retrieve all messages.
    @Override
    public List<Message> getAllMessages() {

        // instantiate an ArrayList to store messages to be returned.
        List<Message> messageList = new ArrayList<>();

        try {

            // SQL query to retrieve all records from the 'message' table.
            String sql = "SELECT * FROM message";

            // PreparedStatement to set variables and execute the sql statements.
            PreparedStatement ps = conn.prepareStatement(sql);

            // executeQuery() to return a result set from the database.
            ResultSet rs = ps.executeQuery();

            // while 'rs' has records (true) add the records to the 'messageList' then return 'messageList'.
            while(rs.next()){
                Message msg = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messageList.add(msg);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return messageList;
    }

    // DAO method implementation to retrieve a particular message by message id.
    @Override
    public Message getMessageByID(int messageID) {

        // message object to be returned.
        Message retrievedmessage = new Message();

        try {

            // SQL query to retrieve a particular message by message id.
            String sql = "SELECT * FROM message WHERE message_id = ?";

            // PreparedStatement to set the variables and execute the sql statements.
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, messageID);

            // executeQuery() to return a result set from the database.
            ResultSet rs = ps.executeQuery();

            // if 'rs' is empty (false) return 'null', otherwise set 'message_id', 'posted_by', 
            // 'message_text', and 'time_posted_epoch' in 'retrievedMessage' from 'rs' values then return 'retrievedMessages'.
            if (!rs.next()) {
                return null;
            } else {
                retrievedmessage.setMessage_id(rs.getInt("message_id"));
                retrievedmessage.setPosted_by(rs.getInt("posted_by"));
                retrievedmessage.setMessage_text(rs.getString("message_text"));
                retrievedmessage.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retrievedmessage;
    }

    // DAO method implementation to delete a particular message by message id.
    @Override
    public Message deleteMessageByID(int messageID) {

        // retrieve message for a given message id.
        Message targetMessage = getMessageByID(messageID);

        try {

            // SQL query to delete a message by a given message id.
            String sql = "DELETE FROM message WHERE message_id = ?";

            // PreparedStatement to set the variables and execute the sql statements.
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, messageID);

            // executeUpdate() to return number of rows affected in the database.
            int numOfRows = ps.executeUpdate();

            // if 'numOfRows' is equals to '0' return 'null', return the deleted 'targetMessage' otherwise.
            if (numOfRows == 0)  return null;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
       return targetMessage;
    }

    // DAO method implementation to update a message by message id. (object param)
    @Override
    public Message updateMessageByID(Message message) {

        try {

            // SQL query to update a message by message id.
            String sql1 = "UPDATE  message SET message_text = ? WHERE message_id = ?";

            // PreparedStatement to set the variables and execute the sql statements.
            PreparedStatement ps = conn.prepareStatement(sql1);
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, message.getMessage_id());

            // executeUpdate() to return number of rows affected in the daatabase.
            int numOfRows = ps.executeUpdate();

            // if 'numOfRows' is '0' return 'null', return 'message' otherwise.
            if (numOfRows == 0) return null;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    // DAO method implementation to update a message by message id. (id and text param)
    @Override
    public Message updateMessageByID(int messageID, String text) {

        // retrieve Message object to be updated with the given message id.
        Message targetMessage = getMessageByID(messageID);

        try {

            // SQL query to update a message by message id.
            String sql1 = "UPDATE  message SET message_text = ? WHERE message_id = ?";

            // PreparedStatement to set the variables and execute the sql statements.
            PreparedStatement ps = conn.prepareStatement(sql1);
            ps.setString(1, text);
            ps.setInt(2, messageID);

            // executeUpdate() to return number of rows affected in the database.
            ps.executeUpdate();

            // SQL query to retrieve a record for given message id and text values.
            String sql2 = "Select * FROM message WHERE posted_by = ? AND message_text = ?";

            PreparedStatement ps1 = conn.prepareStatement(sql2);
            ps1.setInt(1, targetMessage.getPosted_by());
            ps1.setString(2, text);

            // executeQuery() to return a result set from the database.
            ResultSet rs = ps1.executeQuery();

            // if 'rs' is empty (false) return 'null', otherwise set 'message_id', 'posted_by', 
            // 'message_text', and 'time_posted_epoch' in 'targetMessage' from rs values then return 'targetMessage'.
            if (!rs.next()) {
                return null;
            } else {
                targetMessage.setMessage_id(rs.getInt("message_id"));
                targetMessage.setPosted_by(rs.getInt("posted_by"));
                targetMessage.setMessage_text(rs.getString("message_text"));
                targetMessage.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return targetMessage;
    }

    // DAO method implementation to retrieve all messages of a particular account.
    @Override
    public List<Message> getAccountMessages(int accountID) {
        // create new ArrayList to store messages to be returned.
        List<Message> messagesList = new ArrayList<>();

        try {

            // SQL query to retrieve messages for a particular account.
            String sql = "SELECT * FROM message WHERE posted_by = ?";

            // PreparedStatement to set variables and execute the sql statements.
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountID);

            // executeQuery() to return a result set from the database.
            ResultSet rs = ps.executeQuery();

            // while 'rs' has records (true) add the records to the 'messageList' then return 'messageList'.
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messagesList.add(message);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return messagesList;    
    }
    
}
