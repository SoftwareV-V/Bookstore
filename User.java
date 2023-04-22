import java.util.HashMap;

public class User {
    String username;
    String password;
    double balance;
    HashMap<String, Book> myLibrary;
    
    public User(String username, String password, double balance,HashMap<String,Book> library){
        this.myLibrary = library;
        this.username = username;
        this.password = password;
        this.balance = balance;
        
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }  
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }  
    public double getBalance(){
        return this.balance;
    }
    public void setBalance(double setBalance){
        this.balance = balance;
    }
    public HashMap<String, Book> getLibrary(){
        return this.myLibrary;
    }
    public boolean hasBook(String title){
        return this.myLibrary.containsKey(title);
    }
    public String toString(){
        String userStr = "Username: " + this.username + "\nPassword: " + this.password + "\nBalance: $" + String.format("%.2f", this.balance);
        return userStr;
    }

}
