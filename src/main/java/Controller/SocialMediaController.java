package Controller;

import static org.mockito.Mockito.lenient;

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
    String username;
    public SocialMediaController(){
        this.messageServices = new MessageServices();
        this.accountServices = new AccountService();
    }
    public SocialMediaController(String username){
        this.username = username;
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postNewAccountHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.post("/messages", this::postNewMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.put("/messages/{message_id}", this::updateMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountHandler);        
        
        app.start(8080);
        return app;
    }

    private void postNewAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountServices.addAccount(account);
        if(account.username != null && account.password.length() <= 4){
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }
    private void postNewMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageServices.addMessage(message); 
        if(message.message_text != null && message.message_text.length() < 255 ){
            ctx.json(mapper.writeValueAsString(addedMessage));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx){
        List<Message> allMessages = messageServices.getAllMessages();
        ctx.json(allMessages);
        ctx.status(200);
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int m_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageServices.updateMessage(m_id, message);
        if(message.message_text != null && message.message_text.length() < 255){
            ctx.json(mapper.writeValueAsString(updatedMessage));
            ctx.status(200);
        }else{

        }
    }

    private void getMessageByIdHandler(Context ctx){
        String m_id = ctx.pathParam("message_id");
        Message message = messageServices.getMessageById(m_id);
        ctx.json(message);
        ctx.status(200);
    }

    private void deleteMessageHandler(Context ctx){
        String m_id = ctx.pathParam("message_id");
        Message deleteMessage = messageServices.deleteMessage(m_id);
        if(deleteMessage != null){
            ctx.json(deleteMessage);
            ctx.status(200);
        }
    }
    private void getAllMessagesByAccountHandler(Context ctx){
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> getMessagesByAccount = messageServices.getMessageByAccount(accountId);
        ctx.json(getMessagesByAccount);
        ctx.status(200);
    }
}