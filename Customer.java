import java.util.HashMap;

public class Customer {
    String username;
    String password;
    double balance;
    HashMap<String, Book> myLibrary;

    public Customer(String username, String password, double balance,HashMap<String,Book> library){
        this.myLibrary = library;
        this.username = username;
        this.password = password;
        this.balance = balance;
        
    }
    public void displayBooks(){
        //value is the book, key is just the title
        this.myLibrary.forEach((key, value) -> {
            System.out.println(value);
            //print book review if it has one
            if(value.getReviews().containsKey(this.username)){
                System.out.println("Review: " + value.getReview(this.username));
            }
            //print book rating if it has one
            if(value.getRatings().containsKey(this.username)){
                System.out.println("Rating: " + value.getRatings().get(this.username));
            }
            System.out.println();
          });
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
