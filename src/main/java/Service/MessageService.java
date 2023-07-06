package Service;

import DAO.AccountDAOImpl;
import DAO.MessageDAOImpl;
import Model.Account;
import Model.Message;

import java.util.List;

public class MessageService {

    // MessageDAOImpl object variable declaration 
    private MessageDAOImpl msgDAOImpl;


    // MessageService class constructor for class instantiation (no parameter).
    public MessageService(){
        msgDAOImpl = new MessageDAOImpl();
    }

    // MessageService class constructor (with parameter)
    public MessageService(MessageDAOImpl msgDAOImpl){
        this.msgDAOImpl = msgDAOImpl;
    }

    // Service method to add a new message (with input validation).
    public Message addMessage(Message newMessage) {

        // create new AccountDAOimpl object.
        AccountDAOImpl accountDAOimpl = new AccountDAOImpl();

        // call accountDAOImpl's 'getAccountByID'  method to retrieve target account for account verification.
        Account targetAccount = accountDAOimpl.getAccountByID(newMessage.getPosted_by());

        // if 'targetAcct' is 'null', or message text is blank or exceeds 254 characters return 'null'.
        if ((targetAccount == null) || newMessage.getMessage_text().isBlank()
                || newMessage.getMessage_text().isBlank()
                || newMessage.getMessage_text().isEmpty()
                || (newMessage.getMessage_text().length() > 254)) {
            return null;
        }

        // call msgDAOImpl's 'insertNewMessage' method then return 'newMessage' if successful, otherwise, result is 'null'.
        return msgDAOImpl.insertNewMessage(newMessage);
    }

    // Service method to get all messages.
    public List<Message> getAllMessages() {

        // call msgDAOImpl's 'getAllMessages', store returned List in 'messageList' then return 'messageList'.
        List<Message> messageList = msgDAOImpl.getAllMessages();
        return messageList;
    }

    // Service method to get a message by messaage id.
    public Message getMessageByID(int msgID) {

        // call msgDAOImpl's 'getMessageByID' method then return the retrieved Message object if successful, ohterwise, result is 'null'.
        return msgDAOImpl.getMessageByID(msgID);
    }

    // Service method to delete a message by id. 
    public Message deleteMessageByID(int msgID) {

        // call msgDAOImpl's 'deleteMessageByID' method then return the deleted Message object if successful, otherwise, result is 'null'.
        return msgDAOImpl.deleteMessageByID(msgID);
    }

    // Service method to update a message by id.
    public Message updateMessageByID(int msgID, String text) {

        // call msgDAOImpl's 'getAccountByID' method for validation.
        Message targetMsg = msgDAOImpl.getMessageByID(msgID);

        // if 'targetMsg' is 'null', the message text is blank or exceeds 254 characters return 'null',
        if ((targetMsg == null)
                || (text.isBlank())
                || (text.isEmpty())
                || (text.length() > 254)) {

            return null;
        }

        // call msgDAOImpl's 'updateNewMessage' method then return the updated Message object if successful, otherwise, result is 'null'.
        return msgDAOImpl.updateMessageByID(msgID, text);
    }

    // Service method to update a message by message id. 
    public Message updateMessage(Message message) {

        // retrieve message using accountDAOImpl's getAccountByID method for validation.
        Message targetMsg = msgDAOImpl.getMessageByID(message.getMessage_id());

        // if targetMsg is null, the message is blank or exceeds 254 characters return 'null'.
        if ((targetMsg == null)
                || (message.getMessage_text().isBlank())
                || (message.getMessage_text().isEmpty())
                || (message.getMessage_text().length() > 254)) {

            return null;
        }

        // call msgDAOImpl's 'insertNewMessage' method then return updated Messaged object if successful, otherwise, result is 'null'.
        return msgDAOImpl.updateMessageByID(message);
    }

    // Service method to get all messages for a particular account. 
    public List<Message> getAccountMessages(int acctID) {

        // call msgDAOImpl's 'getAccountMessages' method then return the List of messages.
        return msgDAOImpl.getAccountMessages(acctID);
    }

}
