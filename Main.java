import java.util.HashMap;
import java.util.Scanner;
import java.util.Random;

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
                    runLogIn(userInput, db);
                    break;
                //user chooses to register
                case 2:
                    createUser(db, userInput);
                    break;
                //user chooses to exit system
                case 3:
                    runMM = false;
                    break;
                default:
                    System.out.println("Please choose a right option 1-3");
            }
        }
        db.updateCustomersInfo();
    }
    //procedure that handles the log in option in the main menu
    public static void runLogIn(Scanner userInput, Database db){
        System.out.println("Enter the folowing infromation");
        System.out.println("Username:");
        String username = userInput.nextLine();
        System.out.println("Password:");
        String password = userInput.nextLine();
        //check if user is valid
        if(validateUser(db,username,password)){
            runCustomerMenu(db, username, userInput);
        }else if(username.equals("0") && password.equals("0")){
            //owner side of the system
            //should be able to edit book data
            runOwnerMenu(userInput, db);
        }else{
            //user was not found, redirect to main menu
            System.out.println("No user found");
            System.out.println("returning to main menu in ");
        }
    }
    //handles the interface between customer and system
    public static void runCustomerMenu(Database db, String username, Scanner userInput){
        Customer currentUser = db.getCustomerRef(username);
            //runs until user signs out
            while(true){
                printUserMenu(currentUser);
                int userMenuSelection = userInput.nextInt();
                userInput.nextLine();
                switch(userMenuSelection){
                    //user wants to see book he owns
                    case 1:
                        runCustomerLib(currentUser, userInput);
                    break;
                    //user chooses to go to store
                    case 2:
                        runStore(db, currentUser, userInput);
                    break;
                    case 3:
                        //prints out the user information
                        printCustomerInfo(currentUser);
                    break;
                    case 4:
                        //return to caller
                        return;
                    default:
                        System.out.println("Choose an option 1-5");
                }
            }
    }
    //used for the interface between owner and system
    public static void runOwnerMenu(Scanner userInput, Database db){
        //runs until owner chooses to exit
        while (true) {
            printOwenrMenu();
            int ownerOption = userInput.nextInt();
            userInput.nextLine();
            switch (ownerOption) {
                //owner chooses to add a book to store
                case 1:
                        addBookToStore(userInput, db);
                    break;
                //owner chooses to remove a book from the store
                case 2:
                        removeBookFromStore(userInput, db);
                    break;
                case 3:
                    return;
                default:
                    break;
            }
        }
        
    }
    //handles the proceedure of adding a book to the store by the owner
    public static void addBookToStore(Scanner userInput,  Database db){
        //as owenr for book info and ensure input is valid
        System.out.println("Enter the title of the book");
        String title = userInput.nextLine();
        System.out.println("Enter the author of the book");
        String author = userInput.nextLine();
        System.out.println("Enter the price of the book");
        double price = userInput.nextDouble();
        //if the price, title, or length are not valid book will not be added
        if(price < 0 || title.length() == 0 || author.length() == 0){
            System.out.println("Error: it seems the title, author, or price of the book is not valid");
            System.out.println("The book will not be added to the store");
            return;
        }else{
            //if book already in store, then do not add and return 
            if(db.getStore().containsKey(title)){
                System.out.println("Error: Book title already in store");
                return;
            }else{
                db.getStore().put(title, new Book(title, author, price));
                System.out.println("Book succesfully added");
            }
        } 
    }
    //handles the proceedure of removing a book from the store
    public static void removeBookFromStore(Scanner userInput,Database db){
        System.out.println("Enter the title of the book you would like to remove");
        String title = userInput.nextLine();

        //if the title is not found in store then return and don't remove anything
        if(!db.getStore().containsKey(title)){
            System.out.println("Error: Book not found in store");
            return;
        }else{
            //remove book
            db.getStore().remove(title);

            //remove book title from every users library
            db.getCustomers().forEach((key,customer)->{
                customer.getLibrary().remove(title);
            });
            System.out.println("Book succesfully removed");
        }
        
    }
    //prints the owner menu
    public static void printOwenrMenu(){
        System.out.println("what would you like to do?");
        System.out.println("1.Add Book");
        System.out.println("2.Remove Book");
        System.out.println("3.Sign Out");
    }

    //prints out cusomter information
    //might add formatting later on
    public static void printCustomerInfo(Customer currentUser){
        //if user has books print them
        if(currentUser.getLibrary().size() > 0){
            System.out.println(currentUser);
            System.out.println("Books Owned:");
            currentUser.getLibrary().forEach((key,book)->{
                System.out.println(book);
            });;
        }else{
            //print just the user information if they do not have any books
            System.out.println(currentUser);
        }
    }
    //adds review to shared book ref
    public static void addReview(Book book, String username, String review){
        //empty reviews will not be added
        if(review.length() == 0 || book == null){
            System.out.println("No review added!");
        }else{
            //add the review, key will alwasy be username
            book.getReviews().put(username, review);
            System.out.println("Review added to " + book.getTitle());
        }
    }
    //adds rating to book based on user
    public static void addRating(String username,int ratings, Book book){
        //rating must be between 1 and 5
        //no rating is added if the rating is out of bounds
        if(ratings  < 1 || ratings  > 5){
            System.out.println("No ratings added, rating must be between 1 and 5");
            return;
        }
        book.getRatings().put(username, ratings);
        System.out.println("Rating added to" + book.getTitle());
    }
    //prints main menu
    public static void printMainMenu(){
        System.out.println("Hi, Welcome to <nameOfSystem>, what would you like to do?(1-3)");
        System.out.println("1.Log In");
        System.out.println("2.Register");
        System.out.println("3.Exit");
    }
    //validates user exists in database
    public static boolean  validateUser(Database db, String username, String password){
        //returns true if user is found in database
        if(db.getCustomers().containsKey(username)){
            return db.getCustomers().get(username).getPassword().equals(password);
        }else{
            //returns false if user not found in db
            return false;
        }
    }
    //procedure that handles usee creation
    public static  void createUser(Database db, Scanner userInput){
        System.out.println("Enter Username: ");
        String username = userInput.nextLine();
        //it does not allow duplicate users
        while(isDuplicate(username, db)){
            System.out.println("Username already in use choose another");
            username = userInput.nextLine();
        }
        System.out.println("Enter Password: ");
        String password = userInput.nextLine();
        System.out.println("Enter Credit Score: ");
        int creditScore = userInput.nextInt();
        db.getCustomers().put(username,new Customer(username, password, generateBalance(creditScore), new HashMap<>()));
    }
    //check if a username is duplicate or not
    public static boolean isDuplicate(String username, Database db){
        //if the username is found in database or it is 0
        //let user know it is already taken
        if(db.getCustomers().containsKey(username) || username.equals("0")){
            System.out.println("Username already in use choose another");
            return true;
        }else{
            //return true since the username is valid
            System.out.println("Username will be added, and won't be reused");
            return false;
        }
    }
    public static double generateBalance(int creditScore){
        double balance = 0;
        Random r = new Random();
        if(creditScore >= 650 && creditScore <= 850){
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
        }else if(creditScore > 0){
            double low = 25;
            double high = 2500;
            balance = r.nextDouble(high-low) + low;
        }
        return balance;
    }
    public static void printUserMenu(Customer customer){
        System.out.println("--------------------------------------------------------------");
        System.out.println("Hi " + customer.getUsername() + " what would you like to do today?");
        System.out.println("1.View owned books");
        System.out.println("2.Buy books");
        System.out.println("3.View Account");
        System.out.println("4.Sign Out");
        System.out.println("--------------------------------------------------------------");
    }
    
    //handles the procedure of selling a book to the customer
    public static void sellBook(Customer customer, Book book, String title){
        if(book.getPrice() > customer.getBalance()){
            System.out.println("Error: not enough funds");
        }else{
            //add book to users library
            customer.getLibrary().put(title, book);
            //update users balance
            double userBalance = customer.getBalance();
            customer.setBalance(userBalance - book.getPrice());
            System.out.println("Thanks for your purchase, returning to store");
        }
    }
    //proceure that hanles the customer library portion
    //here they can write reviews and give ratings
    public static void runCustomerLib(Customer currentUser, Scanner userInput){
        boolean viewBookMenu = true;
        while(viewBookMenu){
            currentUser.displayBooks();
            displayCusomerLibMenu();
            int bookOptionSelection = userInput.nextInt();
            userInput.nextLine();
            switch(bookOptionSelection){
                //exit book menu
                case 3:
                    viewBookMenu = false;
                break;
                default:
                    //method handles the selection of the user
                    //1 is for reviewing
                    //2 is for rating
                    bookOptionHandler(userInput, currentUser, bookOptionSelection);
            }   
        }
    }
    //handles the options of reviewing and rating a book
    public static void bookOptionHandler(Scanner userInput, Customer currentUser, int option){
        System.out.print("Enter the titile of the book you would like to review: ");
        String bookTitle = userInput.nextLine();
        //keep asing the user for the book title unless they select to exit
        while(true){
            if(!currentUser.getLibrary().containsKey(bookTitle) && !bookTitle.equals("Exit")){
                System.out.println("Book not in library, re-enter title of Exit to leave");
                bookTitle = userInput.nextLine();
                continue;
            }break;
            
        }
        //if the user chooses no to exit and enters 1, they can review the book
        if(!bookTitle.equals("Exit")){
            Book bookRef = currentUser.getLibrary().get(bookTitle);
            String overwriteOption = "";
            //option 1, user chooses to review
            if(option == 1){
                //check if the user already wrote review
                if(bookRef.getReviews().containsKey(currentUser.getUsername())){
                    System.out.println("This book already has a review, would you like to overwrite?(yes or no)");
                    overwriteOption = userInput.nextLine();
                    //if review has been written ask if they would like to overwrite
                    while(!overwriteOption.equalsIgnoreCase("yes") && !overwriteOption.equalsIgnoreCase("no")){
                        System.out.println("Enter yes or no");
                        overwriteOption = userInput.nextLine();
                    }
                }
                //if user wants to overwrite or its their first time reviewing book
                //they are asked to review
                if(overwriteOption.equalsIgnoreCase("yes") || overwriteOption.length() == 0){
                    System.out.println("Please enter your review:");
                    String review = userInput.nextLine();
                    addReview(bookRef, currentUser.getUsername(), review);
                }else{
                    System.out.println("No review addes returning to main menu");
                }
            }else{
                //if the user chooses not to exit and chooses option 2 they get to rate the book
                //check if the user already wrote review
                if(bookRef.getRatings().containsKey(currentUser.getUsername())){
                    System.out.println("This book already has a rating, would you like to overwrite?(yes or no)");
                    overwriteOption = userInput.nextLine();
                    //if review has been written ask if they would like to overwrite
                    while(!overwriteOption.equalsIgnoreCase("yes") && !overwriteOption.equalsIgnoreCase("no")){
                        System.out.println("Enter yes or no");
                        overwriteOption = userInput.nextLine();
                    }
                }
                //if user wants to overwrite or its their first time RATING book
                //they are asked to rate the book
                if(overwriteOption.equalsIgnoreCase("yes") || overwriteOption.length() == 0){
                    System.out.println("Please enter your ratings:");
                    int rating = userInput.nextInt();
                    addRating(currentUser.getUsername(), rating, bookRef);
                }else{
                    System.out.println("No rating added returning to main menu");
                }

            }
            
        }
    }
    //prints the menu inside the custtomer menu
    public static void displayCusomerLibMenu(){
        System.out.println("What would you like to do?");
        System.out.println("1.Add review to book");
        System.out.println("2.Add Rating to book");
        System.out.println("3.Return to main menu");
    }
    //handles the proceedure of the user being inside the store
    public static void runStore(Database db,Customer currentUser, Scanner userInput){
        boolean runStoreMenu = true;
        while(runStoreMenu){
            //displays whole store and should allow option to buy book
            db.displayStore();
            System.out.println("Enter the title of the book you would like to see, or 'Exit' to leave store");
            String userTitle = userInput.nextLine();
            //esure user enters something that is appropiate
            while(true){
                if(userTitle.equals("Exit")){
                    break;
                }else if(!db.getStore().containsKey(userTitle)){//title is not found
                    System.out.println("Title not found, please enter the title or 'Exit' to leave store");
                    userTitle = userInput.nextLine();
                }else if(currentUser.getLibrary().containsKey(userTitle)){//user already owns book
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
    }
}
