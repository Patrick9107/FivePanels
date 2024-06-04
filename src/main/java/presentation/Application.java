package presentation;

import domain.User.User;
import repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Application {

    private static final Scanner sc = new Scanner(System.in);

    private static User loggedInAsUser = null;

    public static void main(String[] args) {
        User testUser = new User("Jakubwachal@gmail.com","test".toCharArray(), "Jakub", "Dr", "Austria");
        start();
    }

    private static void start() {
        if (loggedInAsUser == null) {
            banner("Welcome to FivePanels");
            System.out.println("Pick an action");
            System.out.println();
            System.out.println("1 - Register");
            System.out.println();
            System.out.println("2 - Login");
            loginOrRegister();
        } else {
            userAction();
        }
    }

    // login and register ----------------------------------------------------------

    private static void loginOrRegister() {
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (!input.equals("1") && !input.equals("2")) {

                System.out.println("Invalid Action, try again");
                loginOrRegister();
            }
            if (input.equals("1")) {
                register();
            }
            if (input.equals("2")) {
                login();
            }
        }
    }

    private static void login() {
        String email = null, password = null;
        banner("Login");
        System.out.println("Please enter credentials");
        System.out.println();
        System.out.print("Email: ");
        if (sc.hasNextLine()) {
            email = sc.nextLine();
        }
        System.out.print("Password: ");
        if (sc.hasNextLine()) {
            password = sc.nextLine();
        }

        Optional<User> user = UserRepository.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().getPassword().checkPasswords(password.toCharArray(), user.get().getPassword().getHashedPassword())) {
                loggedInAsUser = user.get();
                System.out.println("Successfully logged in");
                userAction();
            } else {
                System.out.println("Email or Password is not correct. Please try again");
                System.out.println("You will be redirected shortly");
                sleep(2);
                start();
            }
        } else {
            System.out.println("Email or Password is not correct. Please try again");
            System.out.println("You will be redirected shortly");
            sleep(2);
            start();
        }
    }

    //TODO check password strength
    private static void register() {
        String email = null, name = null, title = null, location = null;
        char[]  password = null;
        banner("Register");
        System.out.println("Please enter credentials");
        System.out.println();
        System.out.print("Email: ");
        if (sc.hasNextLine()) {
            email = sc.nextLine();
        }
        System.out.print("Password: ");
        if (sc.hasNextLine()) {
            password = sc.nextLine().toCharArray();
        }
        System.out.print("Name: ");
        if (sc.hasNextLine()) {
            name = sc.nextLine();
        }
        System.out.print("Title: ");
        if (sc.hasNextLine()) {
            title = sc.nextLine();
        }
        System.out.print("Location: ");
        if (sc.hasNextLine()) {
            location = sc.nextLine();
        }
        try {
            User user = new User(email, password, name, title, location);
            loggedInAsUser = user;
            System.out.println();
            System.out.println("User creation successful!");
            System.out.println("You will be redirected shortly");
            sleep(2);
            userAction();
        } catch (Exception e) {
            System.out.println("User creation unsuccessful. Please try again");
            System.out.println("You will be redirected shortly");
            e.printStackTrace();
            sleep(2);
            start();
        }
    }

    // chats ----------------------------------------------------------

    //TODO finish this method after add friend interface and methods!
    private static void chats(){
        banner("Chats");
        exitText();
        AtomicInteger counter = new AtomicInteger(1);

        loggedInAsUser.getChats().forEach(chat -> {
            System.out.println(counter.getAndIncrement() + " - " + chat.getName());
        });

        System.out.println("Press C to create a new chat!");
        if (sc.hasNextLine()){
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("c")){
                // new chat
            }
        }
    }

    // friends ----------------------------------------------------------

    private static void friends(){
        banner("Friends");
        exitText();
        System.out.println("1 - Add a new friend");
        System.out.println();
        System.out.println("2 - Friend Requests");
        System.out.println();
        System.out.println("3 - Friend List");
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            switch (input) {
                case "1":
                    addFriend();
                    break;
                case "2":
                    friendRequests();
                    break;
                case "3":
                    friendList();
                    break;
                case "e", "E":
                    userAction();
                    break;
                default:
                    System.out.println("Invalid Action. Try again");
                    sleep(1);
                    friends();
            }
        }
    }

    public static void addFriend(){
        banner("Add Friends");
        exitText();
        System.out.println("Name of the doctor you want to add: ");
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            if(input.equalsIgnoreCase("e")){
                friends();
            }else {
                if (UserRepository.findByName(input).isEmpty()){
                    System.out.println("No doctor with such name");
                    sleep(2);
                    addFriend();
                }else {
                    UserRepository.findByName(input).forEach(user -> System.out.println(user.getProfile().getName() + " - " + user.getEmail().getAddress()));
                }
            }
        }
    }

    private static void friendRequests() {
        banner("Friend Requests");
        exitText();

        AtomicInteger counter = new AtomicInteger(1);
        Map<Integer,User> counterWithUser = new HashMap<>();

        loggedInAsUser.getSocials().getIncomingFriendRequests().forEach(request -> {
            System.out.println(counter.getAndIncrement() + " - ");
            UserRepository.findById(request).ifPresent(user -> {
                System.out.print(user.getProfile().getTitleAndName());
                counterWithUser.put(counter.get(), user);
            });
        });

        if(sc.hasNextLine()){
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("e")){
                friends();
            } else {
                try {
                    if (Integer.parseInt(input) > 0 && Integer.parseInt(input) <= counter.get()){
                        exitText();
                        System.out.println("1 - accept");
                        System.out.println("2 - decline");
                        if (sc.hasNextLine()){
                            String action = sc.nextLine();
                            switch (action) {
                                case "1":
                                    loggedInAsUser.acceptFriendRequest(counterWithUser.get(Integer.parseInt(input)));
                                    System.out.println("Friend request from " + counterWithUser.get(Integer.parseInt(input)).getProfile().getTitleAndName() + "was accepted.");
                                    sleep(2);
                                    friends();
                                    break;
                                case "2":
                                    loggedInAsUser.denyFriendRequest(counterWithUser.get(Integer.parseInt(input)));
                                    System.out.println("Friend request from " + counterWithUser.get(Integer.parseInt(input)).getProfile().getTitleAndName() + "was declined.");
                                    sleep(2);
                                    friends();
                                    break;
                                case "e", "E":
                                    friends();
                                    break;
                                default:
                                    System.out.println("Invalid Action. Try again");
                            }
                        }
                    }
                }catch (Exception e){
                    System.out.println("Invalid input. Please enter the number corresponding to the person you want to handle the friend request.");
                    System.out.println("You will be redirected shortly");
                    sleep(2);
                    friendRequests();
                }
            }
        }

    }


    public static void friendList(){
        banner("Friend List");
        exitText();
        if (sc.hasNextLine()){
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("e"))
                friends();
        }
        loggedInAsUser.getSocials().getFriends().forEach(friend -> UserRepository.findById(friend).ifPresent(System.out::println));
    }

    // profile ----------------------------------------------------------

    public static void profile() {
        banner("Profile");
        exitText();
        System.out.println("1 - Name: "+ loggedInAsUser.getProfile().getName());
        System.out.println("2 - Title: " +loggedInAsUser.getProfile().getTitle());
        System.out.println("3 - Location: " + loggedInAsUser.getProfile().getLocation());
        System.out.println("4 - Rating: "  + loggedInAsUser.getProfile().getRating());
        System.out.println();
        System.out.println("Type in a number if you want to edit your profile data");

        if (sc.hasNextLine()){
            String input = sc.nextLine();
            switch (input) {
                case "1":
                    editProfile();
                    break;
                case "e", "E":
                    userAction();
                    break;
            }
        }
    }

    private static void editProfile() {
        // what to edit
        exitText();
        System.out.print("New Name: ");
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            try {

                loggedInAsUser.getProfile().setName(input);
            } catch {

            }
        }
    }

    // handle user actions ----------------------------------------------------------

    private static void userAction() {
        banner("Hello " + loggedInAsUser.getProfile().getTitleAndName() + "!");
        System.out.println("What would you like to do?");
        System.out.println();
        System.out.println("1 - Logout");
        System.out.println();
        System.out.println("2 - Chats");
        System.out.println();
        System.out.println("3 - Profile");
        System.out.println();
        System.out.println("4 - Friends");

        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (input.equals("1")) {
                loggedInAsUser = null;
                System.out.println("Successfully logged out!");
                System.out.println("You will be redirected shortly");
                sleep(2);
                start();
            }

            if (input.equals("2"))
                chats();

            if (input.equals("2"))
                chats();

            if (input.equals("4"))
                friends();
            // todo use switch case
        }
    }

    // helper methods ----------------------------------------------------------

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException _) {
        }
    }

    /**
     * @param toDisplay The String that should be displayed
     */
    private static void banner(String toDisplay){
        System.out.println("\n\n\n");
        System.out.println("########################################");
        System.out.println(toDisplay);
        System.out.println("########################################");
        System.out.println("\n\n\n");
    }

    private static void exitText(){
        System.out.println("PRESS E TO EXIT!");
        System.out.println();
    }
}
