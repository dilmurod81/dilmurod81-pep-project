package Controller;

import static org.mockito.Mockito.lenient;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageServices;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    MessageServices messageServices;
    AccountService accountServices;   
    
    
    public SocialMediaController(){
        this.messageServices = new MessageServices();
        this.accountServices = new AccountService();
    }
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::createNewAccountHandler);
        app.post("/login", this::loginHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountHandler);        
        
        return app;
    }
    
    private void createNewAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);        
        // Account a = accountServices.addAccount(account);
        // List<Account> aList = new ArrayList<>();
        // aList.add(a);
        // String match = "";
        // for (Account ac : aList){
        //     if (ac.getUsername().equals(account.username)){
        //         match = "matching";
        //     }else{
        //         match = "not matching";
        //     }
        //  }
        if(account.username.length() == 0){
            ctx.status(400);
        }else if(account.password.length() < 4){
            ctx.status(400);
        // }else if(match.equals("not matching")){
        //     ctx.status(400);
        // }else{
           }else{   Account addedAccount = accountServices.addAccount(account);
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        }
    }
    private void loginHandler(Context ctx) throws JsonProcessingException {        
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account acc = accountServices.getAccountById(account.account_id);
        if(account.username != null && account.username != account.getUsername()){
            ctx.json(mapper.writeValueAsString(acc));
            ctx.status(200);
        }else{
            ctx.status(401);
        }
    }
    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        if(message.message_text.length() == 0){
            ctx.status(400);
        } else if (message.message_text.length() > 255){
            ctx.status(400);
        } else {
            Message addedMessage = messageServices.addMessage(message); 
            ctx.json(mapper.writeValueAsString(addedMessage));
            ctx.status(200);
        }
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException{
        List<Message> allMessages = messageServices.getAllMessages();
        if(allMessages.isEmpty()){
            System.out.println("");
            ctx.status(200);
        }else{
            ctx.json(allMessages);
            ctx.status(200);
        }
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int m_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageServices.updateMessage(m_id, message);
        if(message.message_text == null && message.message_text.length() > 255 && message.message_id != message.getMessage_id()){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(updatedMessage));
            ctx.status(200);
        }
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException{
        String m_id = ctx.pathParam("message_id");
        Message message = messageServices.getMessageById(m_id);
        ctx.json(message);
        ctx.status(200);
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException{
        int m_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deleteMessage = messageServices.deleteMessage(m_id);
        if(deleteMessage != null){
            ctx.json(deleteMessage);
            ctx.status(200);
        }else {
            System.out.println("");
            ctx.status(200);
        }
    }
    private void getAllMessagesByAccountHandler(Context ctx) throws JsonProcessingException{
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> getMessagesByAccount = messageServices.getMessageByAccount(accountId);
        ctx.json(getMessagesByAccount);
        ctx.status(200);
    }
}