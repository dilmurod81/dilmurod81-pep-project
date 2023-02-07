package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MessageDAO {
    Connection connection = ConnectionUtil.getConnection();
    
    public Message addMessage(Message message){
        try{
            PreparedStatement ps = connection.prepareStatement("insert into message (posted_by, message_text, time_posted_epoch, ) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.posted_by);
            ps.setString(2, message.message_text);
            ps.setLong(3, message.time_posted_epoch);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            while(rs.next()){
                int generated_msg_id = (int) rs.getLong(1);
                Message m = new Message(generated_msg_id,
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getLong(4)
                );
                return m;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public List<Message> getAllMessages(){
        try{
            PreparedStatement ps = connection.prepareStatement("select * from message");
            ResultSet rs = ps.executeQuery();
            List<Message> allMessages = new ArrayList<>();
            while(rs.next()){
                Message newMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                allMessages.add(newMessage);
            }
            return allMessages;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public Message getMessageById(String m_id){
        try{
            PreparedStatement ps = connection.prepareStatement("select * from message where message_id = ?");
            ps.setString(1, m_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
                return message;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public Message updateMessage(int m_id, Message message){
        try{
            PreparedStatement ps = connection.prepareStatement("update message set posted_by = ?, message_text = ?, time_posted_epoch = ? where message_id =?");
            ps.setInt(1, message.posted_by);
            ps.setString(2, message.message_text);
            ps.setLong(3, m_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message m = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
                return m;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public Message deleteMessage(int m_id){
        try{

            //Message m = message.getMessage_id() + message.getPosted_by() + message.getMessage_text() + message.getTime_posted_epoch();         
            PreparedStatement ps = connection.prepareStatement("delete from message where message_id = ?");
            ps.setInt(1, m_id);
            ps.executeUpdate();
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message m = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
                return m;
            }
           
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public List<Message> getMessageByAccount(int accountId){
        try{
            PreparedStatement ps = connection.prepareStatement("select message_id, posted_by, message_text, time_posted_epoch from message, account where message.posted_by = ?");
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            List<Message> accountMessages = new ArrayList<>();
            while(rs.next()){
                Message newMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                accountMessages.add(newMessage);
            }
            return accountMessages;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}