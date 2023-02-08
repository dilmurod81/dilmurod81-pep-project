package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    Connection connection = ConnectionUtil.getConnection();
    public Account addAccount(Account account){
        Account a = new Account();
        try{
            PreparedStatement ps = connection.prepareStatement("insert into account (username, password) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.username);
            ps.setString(2, account.password);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            while(rs.next()){
                int key = rs.getInt(1);                
                a = new Account(key, account.getUsername(), account.getPassword());                           
            }   
        }catch(SQLException e){
            e.printStackTrace();
        }
        return a;
    }
    
    public List<Account> getAllUsers(){
        List<Account> accountList = new ArrayList<>();
        try{
            PreparedStatement ps = connection.prepareStatement("select * from account");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account a = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accountList.add(a);
            }   
        }catch(SQLException e){
            e.printStackTrace();
        }
        return accountList;
    }
    
    public Account getAccountById(int account){
        try{
            PreparedStatement ps = connection.prepareStatement("select * from account where account_id = ?");
            ps.setInt(1, account);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account a = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")                        
                );
                return a;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
