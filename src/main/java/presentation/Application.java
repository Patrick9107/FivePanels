package presentation;

import domain.User.User;
import domain.User.UserException;
import foundation.AssertException;
import repository.UserRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;

public class Application {

    private static final Scanner sc = new Scanner(System.in);

    private static User loggedInAsUser = null;

    public static void main(String[] args) {
        new User("jakub@gmail.com", "test".toCharArray(), "Jakub", "Dr.", "Austria");
        new User("patrick@gmail.com", "test".toCharArray(), "Patrick", "Dr.", "Austria");
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
        char[] password = null;
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
            sleep(1);
            userAction();
        } catch (Exception e) {
            System.out.println("User creation unsuccessful. Please try again");
            System.out.println("You will be redirected shortly");
            sleep(1);
            start();
        }
    }

    // chats ----------------------------------------------------------

    //TODO finish this method after add friend interface and methods!
    private static void chats() {
        banner("Chats");
        exitText();
        AtomicInteger counter = new AtomicInteger(1);

        loggedInAsUser.getChats().forEach(chat -> {
            System.out.println(counter.getAndIncrement() + " - " + chat.getName());
        });

        System.out.println("Press C to create a new chat!");
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("c")) {
                // new chat
            }
        }
    }

    // friends ----------------------------------------------------------

    private static void friends() {
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

    public static void addFriend() {
        banner("Add Friends");
        exitText();
        System.out.println("Name of the doctor you want to add: ");
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("e")) {
                friends();
            } else {
                List<User> userList = UserRepository.findByNameContains(input);
                if (userList.isEmpty()) {
                    System.out.println("No doctor with such name");
                    sleep(1);
                    addFriend();
                } else {
                    AtomicInteger counter = new AtomicInteger(1);
                    userList.forEach(user -> System.out.println(counter.getAndIncrement() + " - " + user.getProfile().getTitleAndName() + " (" + user.getEmail().getAddress() + ")"));
                    System.out.println("Which user do you want to add?");
                    if (sc.hasNextLine()) {
                        String action = sc.nextLine();
                        try {
                            if (Integer.parseInt(action) > 0 && Integer.parseInt(action) <= counter.get()) {
                                loggedInAsUser.addFriend(userList.get(Integer.parseInt(action) - 1));
                                System.out.println("User was successfully added as a friend");
                                sleep(1);
                                friends();
                            }
                        } catch (IndexOutOfBoundsException | NumberFormatException e) {
                            System.out.println("Invalid action. Please try again");
                            sleep(1);
                            addFriend();
                        } catch (UserException e) {
                            System.out.println("Can not add this user");
                            sleep(1);
                            addFriend();
                        }
                    }
                }
            }
        }
    }

    private static void friendRequests() {
        banner("Friend Requests");
        exitText();

        AtomicInteger counter = new AtomicInteger(1);
        Map<Integer, User> counterWithUser = new HashMap<>();

        loggedInAsUser.getSocials().getIncomingFriendRequests().forEach(request -> {
            System.out.print(counter.get() + " - ");
            UserRepository.findById(request).ifPresent(user -> {
                System.out.println(user.getProfile().getTitleAndName() + " (" + user.getEmail().getAddress() + ")");
                counterWithUser.put(counter.getAndIncrement(), user);
            });
        });

        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("e")) {
                friends();
            } else {
                try {
                    if (Integer.parseInt(input) > 0 && Integer.parseInt(input) <= counter.get()) {
                        exitText();
                        System.out.println("1 - accept");
                        System.out.println("2 - decline");
                        if (sc.hasNextLine()) {
                            String action = sc.nextLine();
                            switch (action) {
                                case "1":
                                    loggedInAsUser.acceptFriendRequest(counterWithUser.get(Integer.parseInt(input)));
                                    System.out.println("Friend request from " + counterWithUser.get(Integer.parseInt(input)).getProfile().getTitleAndName() + " was accepted.");
                                    sleep(2);
                                    friends();
                                    break;
                                case "2":
                                    loggedInAsUser.denyFriendRequest(counterWithUser.get(Integer.parseInt(input)));
                                    System.out.println("Friend request from " + counterWithUser.get(Integer.parseInt(input)).getProfile().getTitleAndName() + " was declined.");
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
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter the number corresponding to the person you want to handle the friend request.");
                    System.out.println("You will be redirected shortly");
                    sleep(2);
                    friendRequests();
                }
            }
        }

    }


    public static void friendList() {
        banner("Friend List");
        exitText();
        loggedInAsUser.getSocials().getFriends().forEach(friend -> UserRepository.findById(friend).ifPresent(user -> System.out.println(user.getProfile().getTitleAndName())));
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("e")) {
                friends();
            }
        }
    }

    // profile ----------------------------------------------------------

    public static void profile() {
        banner("Profile");
        exitText();
        System.out.println("1 - Name: " + loggedInAsUser.getProfile().getName());
        System.out.println("2 - Title: " + loggedInAsUser.getProfile().getTitle());
        System.out.println("3 - Location: " + loggedInAsUser.getProfile().getLocation());
        System.out.println("4 - Specialization Hashtags: " + loggedInAsUser.getProfile().getTags()); //todo tostring oder so
        System.out.println("Rating: " + loggedInAsUser.getProfile().getRating());
        System.out.println();
        System.out.println("Type in a number if you want to edit your profile data");

        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            switch (input) {
                case "1", "2", "3":
                    editProfile(input);
                    break;
                case "4":
                    editSpecializations();
                    break;
                case "e", "E":
                    userAction();
                    break;
                default:

            }
        }
    }

    private static void editProfile(String edit) {
        String displayName = switch (edit) {
            case "1" -> "Name";
            case "2" -> "Title";
            case "3" -> "Location";
            default -> null;
        };
        System.out.println("Leave blank if you want to exit");
        System.out.println();
        System.out.print("New " + displayName + ": ");
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (!input.isBlank()) {
                try {
                    switch (edit) {
                        case "1":
                            loggedInAsUser.getProfile().setName(input);
                            System.out.println("Successfully set a new " + displayName + "!");
                            sleep(1);
                            profile();
                            break;
                        case "2":
                            loggedInAsUser.getProfile().setTitle(input);
                            System.out.println("Successfully set a new " + displayName + "!");
                            sleep(1);
                            profile();
                            break;
                        case "3":
                            loggedInAsUser.getProfile().setLocation(input);
                            System.out.println("Successfully set a new " + displayName + "!");
                            sleep(1);
                            profile();
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid action. Please try again");
                    sleep(1);
                    profile();
                }
            } else {
                profile();
            }
        }
    }

    private static void editSpecializations() {
        banner("Specialization Hashtags");
        exitText();
        System.out.println("1 - Add new tag");
        System.out.println();
        System.out.println("2 - Remove existing tag");
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            switch (input) {
                case "1":
                    addTagToProfile();
                    break;
                case "2":
                    removeTagFromProfile();
                    break;
                case "e", "E":
                    profile();
                    break;
                default:
                    System.out.println("Invalid action. Please try again");
                    editSpecializations();
            }
        }
    }

    private static void addTagToProfile() {
        System.out.println("Leave blank if you want to exit");
        System.out.println();
        // woul've printed all hashtags but there are over 250
        System.out.print("Enter the name of the new Hashtag: ");
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (!input.isBlank()) {
                try {
                    loggedInAsUser.getProfile().addTag(input);
                    System.out.println("Successfully added " + input + " to your Hashtags!");
                    sleep(1);
                    editSpecializations();
                } catch (UserException e) {
                    System.out.println("Entered Tag does not exist. Try again");
                    sleep(1);
                    addTagToProfile();
                } catch (Exception e) {
                    System.out.println("Invalid action. Please try again");
                    sleep(1);
                    addTagToProfile();
                }
            } else {
                editSpecializations();
            }
        }
    }

    private static void removeTagFromProfile() {

    }

    // handle user actions ----------------------------------------------------------

    private static void userAction() {
        banner("Hello " + loggedInAsUser.getProfile().getTitleAndName() + "!");
        System.out.println("What would you like to do?");
        System.out.println();
        System.out.println("1 - Logout");
        System.out.println();
        System.out.println("2 - Manage Chats");
        System.out.println();
        System.out.println("3 - Manage Profile");
        System.out.println();
        System.out.println("4 - Manage Friends");

        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            switch (input) {
                case "1":
                    loggedInAsUser = null;
                    System.out.println("Successfully logged out!");
                    System.out.println("You will be redirected shortly");
                    sleep(2);
                    start();
                    break;
                case "2":
                    chats();
                    break;
                case "3":
                    profile();
                    break;
                case "4":
                    friends();
                    break;
                default:
                    System.out.println("Invalid action. Please try again");
                    sleep(1);
                    userAction();
            }
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
    private static void banner(String toDisplay) {
        System.out.println("\n\n\n");
        System.out.println("########################################");
        System.out.println(toDisplay);
        System.out.println("########################################");
        System.out.println("\n\n");
    }

    private static void exitText() {
        System.out.println("PRESS E TO EXIT!");
        System.out.println();
    }
}
