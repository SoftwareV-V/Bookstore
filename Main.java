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
                                //user wants to see book he owns
                                case 1:
                                    boolean viewBookMenu = true;
                                    while(viewBookMenu){
                                        displayBooks(currentUser);
                                        System.out.println("What would you like to do?");
                                        System.out.println("1.Add review to book");
                                        System.out.println("2.Add Rating to book");
                                        System.out.println("3.Return to main menu");
                                        int bookOptionSelection = userInput.nextInt();
                                        userInput.nextLine();
                                        String bookTitle = "";
                                        switch(bookOptionSelection){
                                            //user wants to add review to book
                                            case 1:
                                                System.out.print("Enter the titile of the book you would like to review: ");
                                                bookTitle = userInput.nextLine();
                                                //keep asing the user for the book title unless they select to exit
                                                while(true){
                                                    if(!currentUser.getLibrary().containsKey(bookTitle) && !bookTitle.equals("Exit")){
                                                        System.out.println("Book not in library, re-enter title of Exit to leave");
                                                        bookTitle = userInput.nextLine();
                                                        continue;
                                                    }break;
                                                    
                                                }
                                                if(!bookTitle.equals("Exit")){
                                                    Book bookRef = currentUser.getLibrary().get(bookTitle);
                                                    String overwriteOption = "";
                                                    if(bookRef.getReviews().containsKey(currentUser.getUsername())){
                                                        System.out.println("This book already has a review, would you like to overwrite?(yes or no)");
                                                        overwriteOption = userInput.nextLine();

                                                        while(!overwriteOption.equalsIgnoreCase("yes") && !overwriteOption.equalsIgnoreCase("no")){
                                                            System.out.println("Enter yes or no");
                                                            overwriteOption = userInput.nextLine();
                                                        }
                                                    }
                                                    if(overwriteOption.equalsIgnoreCase("yes") || overwriteOption.length() == 0){
                                                        System.out.println("Please enter your review:");
                                                        String review = userInput.nextLine();
                                                        addReview(bookRef, username, review);
                                                    }else{
                                                        System.out.println("No review addes returning to main menu");
                                                    }
                                                }
                                            break;
                                            //user wants to add rating
                                            case 2:
                                                System.out.print("Enter the titile of the book you would like to rate: ");
                                                bookTitle = userInput.nextLine();
                                                //keep asing the user for the book title unless they select to exit
                                                while(true){
                                                    if(!currentUser.getLibrary().containsKey(bookTitle) && !bookTitle.equals("Exit")){
                                                        System.out.println("Book not in library");
                                                        bookTitle = userInput.nextLine();
                                                        continue;
                                                    }break;
                                                    
                                                }
                                                if(!bookTitle.equals("Exit")){
                                                    Book bookRef = currentUser.getLibrary().get(bookTitle);
                                                    String overwriteOption = "";
                                                    if(bookRef.getRatings().containsKey(currentUser.getUsername())){
                                                        System.out.println("This book already has a rating, would you like to overwrite?(yes or no)");
                                                        overwriteOption = userInput.nextLine();

                                                        while(!overwriteOption.equalsIgnoreCase("yes") && !overwriteOption.equalsIgnoreCase("no")){
                                                            System.out.println("Enter yes or no");
                                                            overwriteOption = userInput.nextLine();
                                                        }
                                                    }
                                                    if(overwriteOption.equalsIgnoreCase("yes") || overwriteOption.length() == 0){
                                                        System.out.println("Please enter your ratings:");
                                                        int rating = userInput.nextInt();
                                                        addRating(username, rating, bookRef);
                                                    }else{
                                                        System.out.println("No rating added returning to main menu");
                                                    }
                                                }
                                            break;
                                            //exit book menu
                                            case 3:
                                                viewBookMenu = false;
                                            break;
                                        }   
                                    }
                                break;
                                //user chooses to go to store
                                case 2:
                                    boolean runStoreMenu = true;
                                    while(runStoreMenu){
                                        //displays whole store and should allow option to buy book
                                        displayStore(db.getStore(),currentUser);
                                        System.out.println("Enter the title of the book you would like to see, or 'Exit' to leave store");
                                        String userTitle = userInput.nextLine();
                                        //esures user cannot enre
                                        while(true){
                                            if(userTitle.equals("Exit")){
                                                break;
                                            }else if(!db.getStore().containsKey(userTitle)){
                                                System.out.println("Title not found, please enter the title or 'Exit' to leave store");
                                                userTitle = userInput.nextLine();
                                            }else if(currentUser.getLibrary().containsKey(userTitle)){
                                                System.out.println("You already own that book, if you would like to view it go to you library");
                                                System.out.println("Please enter the title not in your library or 'Exit' to leave store");
                                                userTitle = userInput.nextLine();
                                            }else{
                                                break;
                                            }
                                        }
                                        if(!userTitle.equals("Exit")){
                                            System.out.println(db.getStore().get(userTitle));
                                            System.out.println("What would you like to do?");
                                            System.out.println("1.Buy Book");
                                            System.out.println("2.Return to store menu");
                                            System.out.println("3.Exit Store");
                                            int storeSelection = userInput.nextInt();
                                            userInput.nextLine();
                                            switch(storeSelection){
                                                case 1:
                                                    sellBook(currentUser, db.getStore().get(userTitle), userTitle);
                                                break;
                                                case 2:
                                                    continue;
                                                case 3:
                                                    runStoreMenu = false;
                                                break;
                                            }
                                        }else{
                                            break;
                                        }
                                    }
                                break;
                                case 3:
                                    System.out.println(currentUser);
                                break;
                                case 4:
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
                //user chooses to exit system
                case 3:
                    runMM = false;
                    break;
                default:
                    System.out.println("Please choose a right option 1-3");
            }
        }
        db.updateUsersInfo();
    }
    //adds review to shared book ref
    public static void addReview(Book book, String username, String review){
        //empty reviews will not be added
        if(review.length() == 0){
            System.out.println("No review added!");
        }else{
            //add the review, key will alwasy be username
            book.getReviews().put(username, review);
            System.out.println("Review added to " + book.getTitle());
        }
    }
    //adds rating to book based on user
    public static void addRating(String username,int ratings, Book book){
        if(ratings  < 1 || ratings  > 5){
            System.out.println("No ratings added, rating must be between 1 and 5");
            return;
        }
        book.getRatings().put(username, ratings);
        System.out.println("Rating added to" + book.getTitle());
    }
    //prints main menu
    public static void printMainMenu(){
        System.out.println("Hi, Welcome to <nameOfSystem>, what would you like to do(1-3)");
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
        System.out.println("3.View Account");
        System.out.println("4.Sign Out");
        System.out.println("--------------------------------------------------------------");
    }
    public static void displayBooks(User user){
        HashMap<String,Book> lib = user.getLibrary();
        //value is the book, key is just the title
        lib.forEach((key, value) -> {
            System.out.println(value);
            //print book review if it has one
            if(value.getReviews().containsKey(user.getUsername())){
                System.out.println("Review: " + value.getReview(user.getUsername()));
            }
            //print book rating if it has one
            if(value.getRatings().containsKey(user.getUsername())){
                System.out.println("Rating: " + value.getRatings().get(user.getUsername()));
            }
            System.out.println();
          });
    }
    public static void displayStore(HashMap<String,Book> store, User user){
        System.out.println("------------------------STORE---------------------------------");
        //value is the book, key is just the title
        //prints books not owned by user
        store.forEach((key, value) -> {
            if(!user.getLibrary().containsKey(key)){
                System.out.println(value);
            }
        });
        System.out.println("--------------------------------------------------------------");
    }
    public static void sellBook(User user, Book book, String title){
        if(book.getPrice() > user.getBalance()){
            System.out.println("Error: not enough funds");
        }else{
            //add book to users library
            user.getLibrary().put(title, book);
            //update users balance
            double userBalance = user.getBalance();
            user.setBalance(userBalance - book.getPrice());
            System.out.println("Thanks for your purchase, returning to store");
        }
    }
}
