import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        Database db = Database.getRef();
        //flag variable, to exit it is set to false
        boolean runMM = true;
        while(runMM){
            printMainMenu();
            int selection = userInput.nextInt();
            userInput.nextLine();
            switch(selection){
                //user chooses to log in
                case 1:
                    System.out.println("Enter the folowing infromation");
                    System.out.println("Username:");
                    String username = userInput.nextLine();
                    System.out.println("Password:");
                    String password = userInput.nextLine();

                    //check if user is valid
                    if(validateUser(db.getUsers(),username,password)){
                        User currentUser = db.getUserRef(username);
                        boolean signed_in = true;
                        while(signed_in){
                            printUserMenu(currentUser);
                            int userMenuSelection = userInput.nextInt();
                            userInput.nextLine();
                            switch(userMenuSelection){
                                case 1:
                                    //shoudl display books
                                break;
                                case 2:
                                    //displays whole store and should allow option to buy book
                                break;
                                case 3:
                                    //write a review for a book
                                break;
                                case 4:
                                    //display user account and maybe edit data
                                break;
                                case 5:
                                    signed_in = false;
                                break;
                                default:
                                    System.out.println("Choose an option 1-5");
                            }
                        }

                    }else{
                        //user was not found, redirect to main menu
                        System.out.println("No user found");
                        System.out.println("returning to main menu in ");
                        for(int i = 3;i>0;i--){
                            System.out.println(i);
                            try{
                                TimeUnit.SECONDS.sleep(1);
                            }catch(InterruptedException ie){
                                System.exit(1);
                            }
                        }
                    }
                    break;
                //user chooses to register
                case 2:
                    createUser(db.getUsers(), userInput);
                    break;
                case 3:
                    runMM = false;
                    break;
                default:
                    System.out.println("Please choose a right option 1-3");
            }
        }
        db.updateUsersInfo();
    }
    
    //prints library
    public static void printMainMenu(){
        System.out.println("Hi, Welcom to <nameOfSystem>, what would you liek to do(1-3)");
        System.out.println("1.Log In");
        System.out.println("2.Register");
        System.out.println("3.Exit");
    }
    //validates user exists in database
    public static boolean  validateUser(HashMap<String,User> users, String username, String password){
        if(users.containsKey(username)){
            return users.get(username).getPassword().equals(password);
        }else{
            return false;
        }
    }
    public static  void createUser(HashMap<String, User> users, Scanner userInput){
        System.out.println("Enter Username: ");
        String username = userInput.nextLine();
        while(users.containsKey(username)){
            System.out.println("Username already in use choose another");
            username = userInput.nextLine();
        }
        System.out.println("Enter Password: ");
        String password = userInput.nextLine();
        System.out.println("Enter Credit Score: ");
        int creditScore = userInput.nextInt();
        users.put(username,new User(username, password, generateBalance(creditScore), new HashMap<>()));
    }
    public static double generateBalance(int creditScore){
        double balance = 0;
        Random r = new Random();
        if(creditScore >= 650){
            double low = 7500;
            double high = 10000;
            balance = r.nextDouble(high-low) + low;
        }else if(creditScore >= 450){
            double low = 5000;
            double high = 7500;
            balance = r.nextDouble(high-low) + low;
        }else if(creditScore >= 250){
            double low = 2500;
            double high = 5000;
            balance = r.nextDouble(high-low) + low;
        }else{
            double low = 25;
            double high = 2500;
            balance = r.nextDouble(high-low) + low;
        }
        return balance;
    }
    public static void printUserMenu(User user){
        System.out.println("--------------------------------------------------------------");
        System.out.println("Hi " + user.getUsername() + " what would you like to do today?");
        System.out.println("1.View owned books");
        System.out.println("2.Buy books");
        System.out.println("3.Write review for book");
        System.out.println("4.View Account");
        System.out.println("4.Sign Out");
        System.out.println("--------------------------------------------------------------");
    }
}
