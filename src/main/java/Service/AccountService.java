package Service;

import Model.Account;

import java.util.List;

import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;
    
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account){
        return accountDAO.addAccount(account);    
    }
    
    public List<Account> getAllUsers(){
        return accountDAO.getAllUsers();
    }

    public Account getAccountById(int account){
        Account a = accountDAO.getAccountById(account);
        return a;
    }
}
