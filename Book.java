import java.util.HashMap;
public class Book{
    private String title;
    private String author;
    private double price;
    private HashMap<String,Integer> ratings;
    private HashMap<String,String> reveiws;

    public Book(String title,String author, double price){
        this.title = title;
        this.author = author;
        this.price = price;
        this.ratings = new HashMap<>();
        this.reveiws = new HashMap<>();
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setAuthor(String author){
        this.author = author;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public String getTitle(){
        return this.title;
    }
    public String getAuthor(){
        return this.author;
    }
    public double getPrice(){
        return this.price;
    }
    public HashMap<String,String> getReviews(){
        return this.reveiws;
    }
    public HashMap<String,Integer> getRatings(){
        return this.ratings;
    }
    public String toString(){
        return "Title: " + this.title + "\nAuthor: " + this.author + "\nPrice: $"  + String.format("%.2f", this.price);
    }
    //gets review based on the username, if not found null is retured
    public String getReview(String username){
        if(this.reveiws.containsKey(username)){
            return this.reveiws.get(username);
        }else{
            return null;
        }
    }

}