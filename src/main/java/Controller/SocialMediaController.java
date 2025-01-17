package Controller;

import static org.mockito.ArgumentMatchers.booleanThat;
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
        String match = "";         
        for(Account a : accountServices.getAllUsers()){
            if(a.getUsername().equals(account.username)){
                match = "is matching";
            }else{
                match = "not matching";
            }
        }
        if(account.username.length() == 0){
            ctx.status(400);
        }else if(account.password.length() < 4){
            ctx.status(400);
        }else if(match.equals("is matching")){
            ctx.status(400);
        }else{   
        Account acc = accountServices.addAccount(account);        
        ctx.json(mapper.writeValueAsString(acc));
        ctx.status(200);
        }
    }
    private void loginHandler(Context ctx) throws JsonProcessingException {        
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);       
        String match = "";
        int acc_id = 0;         
        for(Account a : accountServices.getAllUsers()){
            if(a.getUsername().equals(account.username) && a.getPassword().equals(account.password)){
                match = "is matching";
            }
        }
        for(Account a : accountServices.getAllUsers()){
            if(a.getUsername().equals(account.username)){
                acc_id = a.getAccount_id();
            }
        }
        if(match.equals("is matching")){
            Account acc = accountServices.getAccountById(acc_id);
            ctx.json(mapper.writeValueAsString(acc));
            ctx.status(200);
        }else{
            ctx.status(401);
        }
    }
    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        String match = "";  
        for(Account a : accountServices.getAllUsers()){
            if(a.getAccount_id() == message.getPosted_by()){
                match = "matching";
            }
        }       
        if(message.message_text.length() == 0){
            ctx.status(400);
        } else if (message.message_text.length() > 255){
            ctx.status(400);        
        } else if (match.equals("")){
            ctx.status(400); 
        }else if(match.equals("matching")){            
            Message addedMessage = messageServices.addMessage(message);
            ctx.json(mapper.writeValueAsString(addedMessage));
            ctx.status(200);
        }
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException{
        List<Message> allMessages = messageServices.getAllMessages();
        if(allMessages != null){
            ctx.json(allMessages);
            ctx.status(200);
        }else{
            System.out.println("");
            ctx.status(200);
        }
    }
    
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException{
        int m_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageServices.getMessageById(m_id);
        if(message != null){
            ctx.json(message);
            ctx.status(200);
        }else{
            System.out.println("");
            ctx.status(200);
        }
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException{
        int m_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message toBeDeleted = messageServices.getMessageById(m_id);
        if(toBeDeleted != null){
            ctx.json(toBeDeleted);
            messageServices.deleteMessage(m_id);            
            ctx.status(200);
        }else {
            System.out.println("");
            ctx.status(200);
        }
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int m_id = Integer.parseInt(ctx.pathParam("message_id"));
        String match = "";         
        for(Message m : messageServices.getAllMessages()){
            if(m.getMessage_id() == m_id){
                match = "exist";
            }        
        }
        if(message.message_text.length() == 0){
            ctx.status(400);
        }else if(message.message_text.length() > 255){
            ctx.status(400);
        }else if(match.equals("")){
            ctx.status(400);
        }else if(match.equals("exist")){
            ctx.status(200);
            messageServices.updateMessage(m_id, message);
            Message toBeUpdated = messageServices.getMessageById(m_id);
            ctx.json(toBeUpdated);
        }
    }


    private void getAllMessagesByAccountHandler(Context ctx) throws JsonProcessingException{
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> getMessagesByAccount = messageServices.getMessageByAccount(accountId);
        if(getMessagesByAccount != null){
            ctx.json(getMessagesByAccount);
            ctx.status(200);
        }else{
            System.out.println("");
            ctx.status(200);
        }
    }
}