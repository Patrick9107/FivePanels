package presentation;

import domain.Messenger.Chat;
import domain.User.User;
import domain.User.UserException;
import foundation.AssertException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import repository.UserRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;

public class Application {

    private static final Scanner sc = new Scanner(System.in);
    private static final Log log = LogFactory.getLog(Application.class);

    private static User loggedInAsUser = null;

    public static void main(String[] args) {
        try {
            User user1 = new User("jakub@gmail.com", "test".toCharArray(), "Jakub", "Dr.", "Austria");
            User user2 = new User("patrick@gmail.com", "test".toCharArray(), "Patrick", "Dr.", "Austria");
            user1.addFriend(user2);
            user2.acceptFriendRequest(user1);
            start();
        } catch (Exception e) {
            System.out.println("Oops. Something went wrong");
            System.out.println("You will now be logged out");
            // debugging
            System.out.println("Unhandled Exception");
            e.printStackTrace();
            loggedInAsUser = null;
            start();
        }
    }

    private static void start() {
        clear();
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
            loggedInAsUser = new User(email, password, name, title, location);
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

        /* funktionen:
            chat erstellen (muss ein group chat sein weil die direct chats werden automatisch erstellt)
            in einen chat hinein gehen (viewChat) (man hat möglichkeit eine message zu senden. wenn man nichts eintippt soll man aus dem chat rauskommen)
            message senden (wenn man message eingibt konsole "clearen" und history neu ausgeben)
            member adden
            member removen? (vielleicht owner attribut in chat)
            leave chat (removeMember dafür nutzen)
         */
        banner("Chats");
        AtomicInteger counter = new AtomicInteger(1);

        // wenn man neuen chat macht wird er nicht angezeigt
        loggedInAsUser.getChats().forEach(chat -> {
            System.out.println(counter.getAndIncrement() + " - " + chat.displayName(loggedInAsUser));
        });

        System.out.println();
        System.out.println("Type in a number to view a chat");
        System.out.println();
        System.out.println("C - Create a new chat");
        System.out.println();
        System.out.println("E - Exit");
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            switch (input) {
                case "c", "C":
                    createChat();
                    break;
                case "e", "E":
                    userAction();
                    break;
                default:
                    System.out.println("Invalid action");
                    sleep(1);
                    chats();
            }
        }
    }

    private static void createChat() {
        String name = null;
        Set<UUID> members = new HashSet<>();
        banner("Create new Chat");
        System.out.println();
        System.out.println("Leave blank if you want to exit");
        System.out.print("Name: ");
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (!input.isBlank()) {
                name = input;
            } else {
                chats();
            }
        }
        System.out.println();
        System.out.println("Invite your friends to the chat!");
        members = addMemberToChat(members);
        try {
            loggedInAsUser.createGroupChat(name, members);
            System.out.println("Chat was successfully created");
            sleep(1);
            chats();
        } catch (Exception e) {
            System.out.println("Something went wrong creating your chat. Please try again");
            sleep(1);
            createChat();
        }
    }

    private static Set<UUID> addMemberToChat(Set<UUID> members) {
        clear();
        banner("Add a member");

        System.out.println("People currently in member list:");
        System.out.println("-------------------------------");
        members.forEach(uuid -> UserRepository.findById(uuid).ifPresent(user -> System.out.println(user.getProfile().getTitleAndName() + " (" + user.getEmail().getAddress() + ")")));
        System.out.println("-------------------------------");
        System.out.println();
        System.out.print("Pick a member from the list below:");
        System.out.println();
        System.out.println();
        friendListWithNumber();
        System.out.println();
        System.out.println("Leave blank if you are done.");
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (!input.isBlank()) {
                try {
                    UUID newMember = loggedInAsUser.getSocials().getFriends().get(Integer.parseInt(input) - 1);
                    if (!members.contains(newMember)) {
                        members.add(newMember);
                        StringBuilder sb = new StringBuilder("Added ");
                        UserRepository.findById(newMember).ifPresent(user -> sb.append(user.getProfile().getTitleAndName()));
                        sb.append(" to members list");
                        System.out.println(sb.toString());
                        System.out.println();
                        addMemberToChat(members);
                    } else {
                        System.out.println("This User is already in the members list");
                        sleep(1);
                        addMemberToChat(members);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Try again");
                    sleep(1);
                    addMemberToChat(members);
                } catch (UserException e) {
                    System.out.println("Member is already part of the members. Try again");
                    sleep(1);
                    addMemberToChat(members);
                }
            } else {
                if (members.isEmpty()) {
                    System.out.println("Failed creating chat. Your chat has to have at least one other member");
                    sleep(1);
                    addMemberToChat(members);
                }
            }
        }
        return members;
    }
    private static void viewChat(Chat chat){
        banner("C");
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

    private static void addFriend() {
        banner("Add Friends");
        exitText();
        System.out.println("Search for the doctor you want to add: ");
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
                        System.out.println();
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


    private static void friendList() {
        // todo maybe have the option to open direct chat with friend from here
        banner("Friend List");
        exitText();
        loggedInAsUser.getSocials().getFriends().forEach(friend -> UserRepository.findById(friend).ifPresent(user -> System.out.println(user.getProfile().getTitleAndName() + " (" + user.getEmail().getAddress() + ")")));
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("e")) {
                friends();
            }
        }
    }

    private static void friendListWithNumber() {
        AtomicInteger counter = new AtomicInteger(1);
        loggedInAsUser.getSocials().getFriends().forEach(friend -> UserRepository.findById(friend).ifPresent(user -> System.out.println(counter.getAndIncrement() + " - " + user.getProfile().getTitleAndName() + " (" + user.getEmail().getAddress() + ")")));
    }

    // profile ----------------------------------------------------------

    private static void profile() {
        banner("Profile");
        exitText();
        System.out.println("1 - Name: " + loggedInAsUser.getProfile().getName());
        System.out.println("2 - Title: " + loggedInAsUser.getProfile().getTitle());
        System.out.println("3 - Location: " + loggedInAsUser.getProfile().getLocation());
        System.out.println("4 - Specialization Hashtags: " + loggedInAsUser.getProfile().getTags()); //todo bessere darstellung vielleicht
        System.out.println("Rating: " + loggedInAsUser.getProfile().getRating());
        System.out.println();
        System.out.println("Type in a number to edit your profile data");

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
                    System.out.println("Invalid action. Please try again");
                    sleep(1);
                    profile();
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
        System.out.println();
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
                    System.out.println("Entered tag does not exist or you already have this tag. Try again");
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
        banner("Remove Tag");
        exitText();
        AtomicInteger counter = new AtomicInteger(1);
        loggedInAsUser.getProfile().getTags().forEach(tag -> System.out.println(counter.getAndIncrement() + " - " + tag.toString()));
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            try {
                if (Integer.parseInt(input) > 0 && Integer.parseInt(input) <= counter.get()) {
                    //Um gottes willen frag nicht
                    loggedInAsUser.getProfile().removeTag(loggedInAsUser.getProfile().getTags().stream().toList().get(Integer.parseInt(input) - 1).toString());
                    System.out.println("Tag was removed from your profile!");
                    editSpecializations();
                }
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.out.println("Invalid action. Please try again");
                sleep(1);
                removeTagFromProfile();
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

    private static void clear() {
        for (int i = 0; i < 30; i++) {
            System.out.println("\n");
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
