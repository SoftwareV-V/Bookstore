import java.util.HashMap;
public class Book{
    private String title;
    private String author;
    private double price;
    private HashMap<String,String> reveiws;
    public Book(String title,String author, double price){
        this.title = title;
        this.author = author;
        this.price = price;
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
    public String toString(){
        return "Titiel: " + this.title + "\nAuthor: " + this.author + "\nPrice"  + String.format("%.2f", this.price);
    }

}