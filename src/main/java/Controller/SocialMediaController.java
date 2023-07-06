package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

public class SocialMediaController {

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    // Service object variables declaration 
    AccountService acctService;
    MessageService msgService;

    // constructor for SocialMediaController class (initializes service objects' variables at instantiation).
    public SocialMediaController() {
        this.acctService = new AccountService();
        this.msgService = new MessageService();
    }
 
    // Javalin API
    public Javalin startAPI() {

        Javalin app = Javalin.create();

        // Javalin CRUD methods, paths, and parameters.
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getUserMessagesHandler);

        return app;
    }

    // Handler method to add a new account.
    private void postAccountHandler(Context ctx) throws JsonProcessingException {

        // ObjectMapper converts a JSON object to a java object and vice versa.
        ObjectMapper mapper = new ObjectMapper();

        // convert JSON Account object to a java Account object then pass as an argument for acctService's 'addAcct' method.
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = acctService.addAccount(account);

        // if 'account' was added successfully return 'addedAccount' as a JSON Object and status result '200' (OK success),
        // return status result '400' (Bad Request error) otherwise.
        if(addedAccount!=null){
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    // Handler method to verify log-in.
    private void postLoginHandler(Context ctx) throws JsonProcessingException {

        // ObjectMapper converts a JSON object to a java object and vice versa.
        ObjectMapper mapper = new ObjectMapper();

        // convert JSON Account object to a java Account object then pass as an argument for acctService's 'verifyLogin' method.
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account verifiedAccount = acctService.verifyAccountLogin(account);

        // if 'account' was verified return 'verrifiedAccount' as a JSON Object and status result '200' (OK success),
        // return status result '401' (Unauthorized error) otherwise.
        if(verifiedAccount !=null){
            ctx.json(mapper.writeValueAsString(verifiedAccount));
            ctx.status(200);
        }else{
            ctx.status(401);
        }
    }

    // Handler method to ceate a new message.
    private void postMessageHandler(Context ctx) throws JsonProcessingException {

        // ObjectMapper converts a JSON object to a java object and vice versa.
        ObjectMapper mapper = new ObjectMapper();

        // convert JSON Message object to a java Message object then pass an argument for msgService's 'addMessage' method.
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = msgService.addMessage(message);

        // if 'message' was added return 'addedMessage'as a JSON Object and status result '200' (OK success),
        // return status result '400' (Bad Request error) otherwise.
        if(addedMessage !=null){
            ctx.json(mapper.writeValueAsString(addedMessage));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    // Handler method to retrieve all messages.
    private void getAllMessagesHandler(Context ctx) {

        // create 'messages' list for all messages returned by msgService's 'getAllMessages' method 
        // then return a JSON object.
        List<Message> messages = msgService.getAllMessages();
        ctx.json(messages);
    }
    
    // Handler method to retrieve a message by message_id.
    private void getMessageByIDHandler(Context ctx) throws JsonProcessingException {

        // ObjectMapper converts a JSON object to a java object and vice versa.
        ObjectMapper mapper = new ObjectMapper();

        // convert 'message_id' parameter value into an int value.
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));

        // call msgService's 'getMessageByID' method to retrieve a message for a given message id.
        Message retrievedMsg = msgService.getMessageByID(messageID);

        // if message was retrieved return 'retrievedMsg' as a json Object and status result '200' (OK success),
        // otherwise return status result '200' (OK success) as well.
        if(retrievedMsg != null){
            ctx.json(mapper.writeValueAsString(retrievedMsg));
            ctx.status(200);
        }else{
            ctx.status(200);
        }
    }

     // Handler method to delete a message by message_id.
     private void deleteMessageByIDHandler(Context ctx) throws JsonProcessingException {

         // ObjectMapper converts a JSON object to a java object and vice versa.
        ObjectMapper mapper = new ObjectMapper();

        // convert 'message_id' parameter value into an int value.
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));

        // call msgService's 'deleteMessageByID' method to delete a message for a given message id.
        Message deletedMsg = msgService.deleteMessageByID(messageID);


        // if message was deleted return 'deletedMsg' as a JSON Object and status result '200' (OK success),
        // return status result '200' (OK success) if does not exists as well.
        if(deletedMsg != null){
            ctx.json(mapper.writeValueAsString(deletedMsg));
            ctx.status(200);
        }else{
            ctx.status(200);
        }
    }

     // Handler method to update a message by message_id.
     private void updateMessageByIDHandler(Context ctx) throws JsonProcessingException {

        // ObjectMapper converts JSON object to java object and vice versa.
        ObjectMapper mapper = new ObjectMapper();

        // convert JSON Message object to a java Message object then get its 'message_text' value.
        Message message = mapper.readValue(ctx.body(), Message.class);
        String text = message.getMessage_text();

        // convert the 'message_id' parameter value into an int value.
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        // call msgService's updateMessageByID method to update the message text for a particular message id.
        Message updatedMessage = msgService.updateMessageByID(message_id, text);

        // if message was updated return 'updatedMessage' as a json Object and status result '200' (OK success),
        // return status result '400' (Bad Request error) otherwise.
        if(updatedMessage != null){
            ctx.json(mapper.writeValueAsString(updatedMessage));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }
    
    // Handler method to retrieving messages of a particular account.
    private void getUserMessagesHandler(Context ctx) throws JsonProcessingException {

        // ObjectMapper converts JSON object to java object and vice versa.
        ObjectMapper mapper = new ObjectMapper();

        // convert 'account_id' parameter value into an int value then pass it as argument for MsgService's 'getAcoountMessages' method.
        int accountID = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> accounttMessages = msgService.getAccountMessages(accountID);

        // return 'acctMessages' as a json Object and status result '200' (OK success) even if there are no messages.
        ctx.json(mapper.writeValueAsString(accounttMessages));
        ctx.status(200);
    }

}