package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MessageDAO {
    Connection connection = ConnectionUtil.getConnection();
    
    public Message addMessage(Message message){
        Message m = new Message();
        try{
            PreparedStatement ps = connection.prepareStatement("insert into message (posted_by, message_text, time_posted_epoch) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.posted_by);
            ps.setString(2, message.message_text);
            ps.setLong(3, message.time_posted_epoch);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            while(rs.next()){
                int key = rs.getInt(1);                
                m = new Message(key, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());               
                return m;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return m;
    }
    public List<Message> getAllMessages(){
        List<Message> allMessages = new ArrayList<>();
        try{
            PreparedStatement ps = connection.prepareStatement("select * from message");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message newMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                allMessages.add(newMessage);
            }            
        }catch(SQLException e){
            e.printStackTrace();
        }
        return allMessages;
    }
    public Message getMessageById(int m_id){
        try{
            PreparedStatement ps = connection.prepareStatement("select * from message where message_id = ?");
            ps.setInt(1, m_id);
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
    public Message deleteMessage(int m_id){
        try{
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
    public Message updateMessage(int m_id, Message message){
        try{
            PreparedStatement ps = connection.prepareStatement("update message set message_text = ? where message_id = ?");
            ps.setString(1, message.message_text);            
            ps.setInt(2, m_id);
            ps.executeUpdate();
            return message;
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