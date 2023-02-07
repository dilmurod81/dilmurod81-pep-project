package Service;
import Model.Message;
import DAO.MessageDAO;
import java.util.List;

public class MessageServices {
    private MessageDAO messageDAO;

    public MessageServices(){
        messageDAO = new MessageDAO();
    }

    public MessageServices(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    public Message addMessage(Message message){
        messageDAO.addMessage(message);
        return null;
    }
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }
    public Message getMessageById(String m_id){
        return messageDAO.getMessageById(m_id);
    }
    public Message updateMessage(int m_id, Message message){
        messageDAO.updateMessage(m_id, message);
        return null;
    }
    public Message deleteMessage(int m_id){
        return messageDAO.deleteMessage(m_id);
    }
    public List<Message> getMessageByAccount(int accountId){
        return messageDAO.getMessageByAccount(accountId);
    }
}
