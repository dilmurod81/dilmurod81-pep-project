package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;


public class AccountDAO {
    Connection connection = ConnectionUtil.getConnection();
    
    public void addAccount(Account account){
        try{
            PreparedStatement ps = connection.prepareStatement("insert into account (username, password) values (?, ?)");
            ps.setString(1, account.username);
            ps.setString(2, account.password);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
