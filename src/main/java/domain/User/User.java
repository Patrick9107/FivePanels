package domain.User;

import domain.common.BaseEntity;
import domain.Messenger.Chat;
import domain.Medicalcase.Medicalcase;
import domain.common.Media;
import domain.common.TextContent;
import repository.ChatRepository;
import repository.UserRepository;

import java.util.*;

import static foundation.Assert.*;

public class User extends BaseEntity {
    // not null
    private Email email;
    // not null
    private Password hashedPassword;
    // not null
    private Profile profile;
    // not null
    private Socials socials;
    private boolean verified;
    // not null
    private Set<Chat> chats;
    // not null, exactly 2 entries
    private Map<Ownership, Set<Medicalcase>> medicalcases;

    // TODO NUR ZUM TESTEN, KANN WIEDER GELÖSCHT WERDEN
//    public User() {
//        this.email = new Email("admin@admin.com");
//        this.hashedPassword = new Password("admin".toCharArray());
//        this.profile = new Profile();
//        this.socials = new Socials();
//        verified = false;
//        chats = new LinkedHashSet<>();
//        setMedicalcases();
//    }

    public User(String email, /*TODO probably need to change String to something different*/String password, String name, String title, String location) {
        this.email = new Email(email);
        this.hashedPassword = new Password(password.toCharArray());
        this.profile = new Profile(name, title, location);
        this.socials = new Socials();
        this.chats = new LinkedHashSet<>();
        setMedicalcases();
    }

    public void verify() {
    }

    public Email getEmail() {
        return email;
    }

    public void setMedicalcases() {
        medicalcases = new HashMap<>();
        medicalcases.put(Ownership.OWNER, new HashSet<>());
        medicalcases.put(Ownership.MEMBER, new HashSet<>());
    }

    public void addChat(Chat chat) {
        isNotNull(chat, "chat");
        chats.add(chat);
    }

    public void removeChat(Chat chat) {
        isNotNull(chat, "chat");
        chats.remove(chat);
    }

    public Set<Chat> getChats() {
        return chats;
    }

    public void sendMessage(Chat chat, TextContent content, List<Media> attachments) {
        chat.sendMessage(this, chat, content, attachments);
    }

    public void viewChat(Chat chat) {
        if (!(chat.isGroupChat())) {
            chat.getMembers().stream().filter(uuid ->
                    this.getId() != uuid).findFirst().flatMap(UserRepository::findById).ifPresent(user ->
                    System.out.println(user.getProfile().getName()));
        } else {
            System.out.println(chat.getName());
        }
        chat.getHistory().forEach(System.out::println);
    }

    public Chat getDirectChat(UUID userId) {
        return ChatRepository.findAll().stream().filter(chat -> !chat.isGroupChat() && chat.getMembers().contains(userId)).findFirst().get();
    }
    public Socials getSocials() {
        return socials;
    }

    public Profile getProfile() {
        return profile;
    }

    public void addFriend(User user) {
        this.socials.addFriend(this, user);
    }

    public void acceptFriendRequest(User user) {
        this.socials.acceptFriendRequest(this, user);
    }

    public void denyFriendRequest(User user) {
        this.socials.denyFriendRequest(this, user);
    }

    public void removeFriend(User user) {
        this.socials.removeFriend(this, user);
    }

    public void createMedicalcase(String title, String... tags) {
        medicalcases.get(Ownership.OWNER).add(new Medicalcase(title, this, tags));
    }
}
