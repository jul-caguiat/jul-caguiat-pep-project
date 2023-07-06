package DAO;

import Model.Message;
import java.util.List;

public interface MessageDAO {

    // abstract methods to be implemented.
    public Message insertNewMessage(Message newMessage);
    public List<Message> getAllMessages();
    public Message getMessageByID(int msgID);
    public List<Message> getAccountMessages(int acctID);
    public Message updateMessageByID(Message message);
    public Message updateMessageByID(int msgID, String text);
    public Message deleteMessageByID(int msgID);
    
}
