package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AccountDAO {
    Connection connection = ConnectionUtil.getConnection();
    public Account addAccount(Account account){
        try{
            PreparedStatement ps = connection.prepareStatement("insert into account (username, password) values (?, ?)");
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
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