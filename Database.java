import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.io.IOException;
import java.io.FileWriter;


public class Database {
    private HashMap<String, User> users;
    private HashMap<String,Book> store;
    private static Database ref;

    private Database(){
        this.store = populateStore();
        this.users = populateUsers(store);
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

    //populates the users
    private static HashMap<String, User> populateUsers(HashMap<String,Book> store){
        HashMap<String,User> users = new HashMap<>();
        String fileName = "users.csv";
        File usersFile = new File(fileName);
        Scanner fileReader = null;
        try {
            fileReader = new Scanner(usersFile);
        } catch (FileNotFoundException e) {
            //if no file foun keep running, just that there will be no users
            //will have to create new users
            return new HashMap<>();
        }
        while(fileReader.hasNextLine()){
            String currentLine = fileReader.nextLine();
            String[] splitCurrentLine = currentLine.split(",");
            HashMap<String,Book> library = new HashMap<>();
            //if value is less then 4 then user does not own any books
            if(splitCurrentLine.length < 4){
                users.put(splitCurrentLine[0], new User(splitCurrentLine[0],splitCurrentLine[1],Double.parseDouble(splitCurrentLine[2]),library));

            }else{
                String[] bookTitles = splitCurrentLine[3].split(":");
                //build users current library
                
                for(String title:bookTitles){
                    library.put(title,store.get(title));

                }
                users.put(splitCurrentLine[0], new User(splitCurrentLine[0],splitCurrentLine[1],Double.parseDouble(splitCurrentLine[2]),library));
            }
            
        }
        return users;
    }

    public User getUserRef(String key){
        return this.users.get(key);
    }
    public Book getBookRef(String key){
        return this.store.get(key);
    }
    public HashMap<String, User> getUsers(){
        return this.users;
    }
    public HashMap<String, Book> getStore(){
        return this.store;
    }
    public void displayStore(){
        Set<String> keySet = this.store.keySet();
        Object[] keys = keySet.toArray();
        for (Object key: keys){
            System.out.println(this.store.get((String)key));
        }
    }
    public void updateUsersInfo(){
        Set<String> keySet = this.users.keySet();
        Object[] keys = keySet.toArray();
        try {
            String userLine = "";
            File usersFile = new File("users.csv");
            if (usersFile.exists()) {
                FileWriter myWriter = new FileWriter("users.csv");
                
                for (Object key: keys){
                    //get user
                    User user = this.users.get((String)key);
                    //seperate user info by comma
                    userLine += user.getUsername() + "," + user.getPassword() + "," + user.getBalance() + ",";
                    HashMap<String,Book> userLib = user.getLibrary();
                    //titles are the keys in library and store
                    Set<String> bookKeySet = userLib.keySet();
                    Object[] libKeys = bookKeySet.toArray();
                    //add book titles separated by :
                    for(int i = 0;i < libKeys.length;i++){
                        if(i < libKeys.length - 1){
                            userLine += (String)libKeys[i] + ":";
                        }else{
                            userLine += (String)libKeys[i] + "\n";
                        }
                    }
                    //add formatted line to file
                    myWriter.write(userLine);
                }
                myWriter.close();
            } else {
                usersFile.createNewFile();
                FileWriter myWriter = new FileWriter("users.csv");
                
                for (Object key: keys){
                    //get user
                    User user = this.users.get((String)key);
                    //seperate user info by comma
                    userLine += user.getUsername() + "," + user.getPassword() + "," + user.getBalance() + ",";
                    HashMap<String,Book> userLib = user.getLibrary();
                    //titles are the keys in library and store
                    Set<String> bookKeySet = userLib.keySet();
                    Object[] libKeys = bookKeySet.toArray();
                    //add book titles separated by :
                    for(int i = 0;i < libKeys.length;i++){
                        if(i < libKeys.length - 1){
                            userLine += (String)libKeys[i] + ":";
                        }else{
                            userLine += (String)libKeys[i] + "\n";
                        }
                    }
                    //add formatted line to file
                    myWriter.write(userLine);
                }
                myWriter.close();
            }
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
}
