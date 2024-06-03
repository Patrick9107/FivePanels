package presentation;

import domain.User.Socials;
import domain.User.User;
import org.apache.commons.collections.functors.IfClosure;
import repository.UserRepository;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Application {

    private static final Scanner sc = new Scanner(System.in);

    private static User loggedInAsUser = null;

    public static void main(String[] args) {
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
        String email = null, password = null, name = null, title = null, location = null;
        banner("Register");
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

            }
        }
    }

    private static void friends(){
        banner("Friends");
        exitText();
        System.out.println("1 - add friends");
        System.out.println();
        System.out.println("2 - friend requests");
        System.out.println();
        System.out.println("Your Friends:");
        loggedInAsUser.getSocials().listFriends().forEach(friend -> UserRepository.findById(friend).ifPresent(System.out::println));
        if (sc.hasNextLine()){
            String input = sc.nextLine();
            if(input.equals("1")){

            }

            if (input.equals("2")){

            }
        }
    }

    public static void addFriend(){
        banner("add friends");
        exitText();
        System.out.println("Doctor you want to add: ");
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            if(input.equalsIgnoreCase("e")){
                friends();
                //TODO wenz fragen wie man die addFriend methode beendet, da nach dem friends aufgerufen wird, die addFriend methode weiter läuft und nicht beendet ist
            }else {
                if (UserRepository.findByName(input).isEmpty()){
                    System.out.println("Kein Doktor mit diesem Namen gefunden");
                    sleep(3);
                    //TODO das gleiche hier
                    addFriend();
                }else {
                    UserRepository.findByName(input).forEach(user -> System.out.println(user.getProfile().getName() + " - " + user.getEmail().getAddress()));
                }
            }
        }
    }
    private static void userAction() {
        banner("Hello " + loggedInAsUser.getProfile().getTitle() + " " + loggedInAsUser.getProfile().getName() + "!");
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

            if (input.equals("2")){
                chats();
            }

            if (input.equals("4")){
                friends();
            }
        }

    }

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
