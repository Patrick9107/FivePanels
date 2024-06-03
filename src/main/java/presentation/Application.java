package presentation;

import domain.User.User;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Application {

    private static final Scanner sc = new Scanner(System.in);

    private static User loggedInAsUser = null;

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        if (loggedInAsUser != null) {
            System.out.println("########################################");
            System.out.println("Welcome to FivePanels");
            System.out.println("########################################");
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
        System.out.println("login");
    }

    private static void register() {
        String email = null, password = null, name = null, title = null, location = null;
        System.out.println("\n\n\n");
        System.out.println("########################################");
        System.out.println("Register");
        System.out.println("########################################");
        System.out.println("\n\n\n");
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
            System.out.println("User creation successful!");
            userAction();
        } catch (Exception e) {
            System.out.println("User creation unsuccessful. Please try again");
            System.out.println("You will be redirected shortly");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException _) {
            }
            register();
        }
    }

    private static void userAction() {
    }
}
