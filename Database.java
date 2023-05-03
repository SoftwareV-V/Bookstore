import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.io.IOException;
import java.io.FileWriter;

public class Database {
    private HashMap<String, Customer> customers;
    private HashMap<String,Book> store;
    private static Database ref;

    private Database(){
        this.store = populateStore();
        this.customers = populateCustomers(store);
    }

    public static Database getRef(){
        if(ref == null){
            ref = new Database();
        }
        return ref;
    }
    //method populates the store based on the csv file
    private static HashMap<String,Book> populateStore(){
        HashMap<String,Book> store = new HashMap<>();
        String fileName = "books.csv";
        File bookFile = new File(fileName);
        Scanner fileReader = null;
        //make sure file is in dir
        try{
            fileReader = new Scanner(bookFile);
        }catch(FileNotFoundException fn){
            System.out.println("File not in directory");
            System.exit(0);
        }
        //skip header
        fileReader.nextLine();

        //populate store for every book in file
        while(fileReader.hasNextLine()){
            String currentLine = fileReader.nextLine();
            String[] splitCurrentLine = currentLine.split(",");
            store.put(splitCurrentLine[0], new Book(splitCurrentLine[0],splitCurrentLine[1],Double.parseDouble(splitCurrentLine[2])));
        }

        return store;
    }

    //populates the customers
    private static HashMap<String, Customer> populateCustomers(HashMap<String,Book> store){
        HashMap<String,Customer> customers = new HashMap<>();
        String fileName = "customers.csv";
        File customersFile = new File(fileName);
        Scanner fileReader = null;
        try {
            fileReader = new Scanner(customersFile);
        } catch (FileNotFoundException e) {
            //if no file foun keep running, just that there will be no customers
            //will have to create new customers
            return new HashMap<>();
        }
        while(fileReader.hasNextLine()){
            String currentLine = fileReader.nextLine();
            String[] splitCurrentLine = currentLine.split(",");
            HashMap<String,Book> library = new HashMap<>();
            //if value is less then 4 then customer does not own any books
            if(splitCurrentLine.length < 4){
                customers.put(splitCurrentLine[0], new Customer(splitCurrentLine[0],splitCurrentLine[1],Double.parseDouble(splitCurrentLine[2]),library));

            }else{
                String[] bookTitles = splitCurrentLine[3].split(":");
                //build customers current library
                
                for(String title:bookTitles){
                    library.put(title,store.get(title));

                }
            }
            
        }
        fileReader.close();
        return customers;
    }

    public Customer getCustomerRef(String key){
        return this.customers.get(key);
    }
    public Book getBookRef(String key){
        return this.store.get(key);
    }
    public HashMap<String, Customer> getCustomers(){
        return this.customers;
    }
    public HashMap<String, Book> getStore(){
        return this.store;
    }
    public void displayStore(){
        Set<String> keySet = this.store.keySet();
        Object[] keys = keySet.toArray();
        for (Object key: keys){
            System.out.println(this.store.get((String)key));
            System.out.println();
        }
    }
    public void updateCustomersInfo(){
        Set<String> keySet = this.customers.keySet();
        Object[] keys = keySet.toArray();
        try {
            String customerLine = "";
            File customersFile = new File("customers.csv");
            if (customersFile.exists()) {
                FileWriter myWriter = new FileWriter("customers.csv");
                
                for (Object key: keys){
                    //get customer
                    Customer customer = this.customers.get((String)key);
                    //seperate customer info by comma
                    customerLine += customer.getUsername() + "," + customer.getPassword() + "," + customer.getBalance();
                    HashMap<String,Book> customerLib = customer.getLibrary();
                    //titles are the keys in library and store
                    Set<String> bookKeySet = customerLib.keySet();
                    Object[] libKeys = bookKeySet.toArray();
                    if(libKeys.length > 0){
                        customerLine += ",";
                    }
                    //add book titles separated by :
                    for(int i = 0;i < libKeys.length;i++){
                        if(i < libKeys.length - 1){
                            customerLine += (String)libKeys[i] + ":";
                        }else{
                            customerLine += (String)libKeys[i];
                        }
                    }
                    customerLine += "\n";
                    //add formatted line to file
                    myWriter.write(customerLine);
                }
                myWriter.close();
            } else {
                customersFile.createNewFile();
                FileWriter myWriter = new FileWriter("customers.csv");
                
                for (Object key: keys){
                    //get customer
                    Customer customer = this.customers.get((String)key);
                    //seperate customer info by comma
                    customerLine += customer.getUsername() + "," + customer.getPassword() + "," + customer.getBalance();
                    HashMap<String,Book> customerLib = customer.getLibrary();
                    //titles are the keys in library and store
                    Set<String> bookKeySet = customerLib.keySet();
                    Object[] libKeys = bookKeySet.toArray();
                    if(libKeys.length > 0){
                        customerLine += ",";
                    }
                    //add book titles separated by :
                    for(int i = 0;i < libKeys.length;i++){
                        if(i < libKeys.length - 1){
                            customerLine += (String)libKeys[i] + ":";
                        }else{
                            customerLine += (String)libKeys[i];
                        }
                    }
                    customerLine += "\n";
                    //add formatted line to file
                    myWriter.write(customerLine);
                }
                myWriter.close();
            }
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
}
