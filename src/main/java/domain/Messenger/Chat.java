package domain.Messenger;

import domain.User.User;
import domain.common.BaseEntity;
import domain.common.Media;
import domain.common.TextContent;
import repository.ChatRepository;
import repository.UserRepository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static foundation.Assert.*;

public class Chat extends BaseEntity {

    // can be null if groupchat is false - not null otherwise, not blank, max 64 characters
    private String name;
    private final boolean groupChat;
    // not null, max 512 members, not empty
    private Set<UUID> members;
    // not null
    private List<Message> history;

    public Chat(String name, Set<UUID> members, boolean groupChat) {
        this.groupChat = groupChat;
        setName(name);
        setMembers(members);
        history = new ArrayList<>();
        ChatRepository.save(this);
    }

    public void setName(String name) {
        if (groupChat) {
            hasMaxLength(name, 513, "name");
            this.name = name;
        }
    }

    public void setMembers(Set<UUID> members) {
        hasMaxSize(members, 513, "members");
        this.members = new HashSet<>(members);
    }

    public Chat addMember(User user) {
        isNotNull(user, "user");
        hasMaxSize(members, 513, "members");

        // if the chat is a direct chat (non-groupchat), create a new groupchat
        if (!groupChat) {
            StringBuilder sb = new StringBuilder();
            members.forEach(uuid -> UserRepository.findById(uuid).ifPresent(user1 -> sb.append(user1.getProfile().getName()).append(", ")));
            sb.append(user.getProfile().getName());
            Chat chat = new Chat(sb.toString(), new HashSet<UUID>(members), true);
            user.getChats().add(chat);
            members.forEach(uuid -> UserRepository.findById(uuid).ifPresent(user1 -> user1.getChats().add(chat)));
            return chat;
        }
        if (members.contains(user.getId()))
            throw new MessengerException(STR."addMember(): user is already a member of this chat");
        members.add(user.getId());
        user.getChats().add(this);
        return this;
    }

    public void removeMember(User user) {
        if (!groupChat)
            throw new MessengerException(STR."removeMember(): can not remove a member from a direct chat");
        isNotNull(user, "user");
        hasMaxSize(members, 513, "members");

        if (!(members.contains(user.getId())))
            throw new MessengerException(STR."removeMember(): user is not a member of this chat");
        // you can not create a chat with only yourself, but you can remove a member from a groupchat with only 2 members
        members.remove(user.getId());
        user.getChats().remove(this);
    }

    public void addToHistory(Message message) {
        isNotNull(message, "message");
        history.add(message);
    }

    public void sendMessage(User user, Chat chat, TextContent content, List<Media> attachments) {
        // isnotnull assert in message class for the remaining parameters
        isNotNull(chat, "chat");

        if (!(members.contains(user.getId())))
            throw new MessengerException("sendMessage(): User is not a member of this chat");
        chat.addToHistory(new Message(user, Instant.now(), content, attachments, Status.SENT));
    }

    /**
     * Displays information about a chat.
     * If the chat is not a group chat, it prints the name of the other member.
     * If the chat is a group chat, it prints the chat's name.
     * It also prints the chat's history.
     *
     * @param user the user that wants to view the chat
     * @throws MessengerException if the user is not a member of this chat
     */
    public void viewChat(User user) {
        isNotNull(user, "user");
        if (!(members.contains(user.getId())))
            throw new MessengerException(STR."viewChat(): user is not part of this chat");
        history.forEach(System.out::println);
    }

    public String displayName(User user) {
        if (groupChat) {
            return name;
        }
        AtomicReference<String> displayName = new AtomicReference<>();
        members.stream().filter(uuid ->
                user.getId() != uuid).findFirst().flatMap(UserRepository::findById).ifPresent(user1 ->
                displayName.set(user1.getProfile().getName()));
        return displayName.get();
    }

    public String getName() {
        return name;
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public List<Message> getHistory() {
        return history;
    }

    public boolean isGroupChat() {
        return groupChat;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        history.forEach(sb::append);
        return sb.toString();
    }

    //Test for sending Messages between 2 Users (it actually works) pls dont delete i need this code
//    public static void main(String[] args) {
//        User homer = new User("homer@simpson.com", "password".toCharArray(), "Homer Simpson", "Rh.D.", "United Kingdom");
//        User bart = new User("bart@simpson.com", "password".toCharArray(), "Bart Simpson", "Ph.D.", "United States");
//        User lisa = new User("lisa@simpson.com", "password".toCharArray(), "Lisa Simpson", "Ph.D.", "United States");
//        User test = new User("test@simpson.com", "password", "test Simpson", "Ph.D.", "United States");
//        homer.addFriend(bart);
//        bart.acceptFriendRequest(homer);
//
//        lisa.createGroupChat("theCoolOnes", Set.of(bart.getId(), homer.getId()));
//
//        if (homer.getDirectChat(bart.getId()).isPresent()) {
//            Chat chat = homer.getDirectChat(bart.getId()).get();
//            homer.sendMessage(chat, "Das ist Homer Message test test", List.of(new Media("hallo.txt", "sdhjgvbfd", 100), new Media("test.txt", "sdhjgvbfd", 100)));
//            bart.sendMessage(chat,  "Das ist Bart Message hallo 123 Test", List.of(new Media("hallo.txt", "sdhjgvbfd", 100)));
//            homer.viewChat(chat);
//        }
//
//        Chat chat = ChatRepository.findByName("theCoolOnes").get(0);
//        homer.sendMessage(chat, new TextContent("Das ist Homer Message hallo 123 Test"), null);
//        lisa.viewChat(chat);
//        homer.viewChat(chat);
//        bart.viewChat(chat);
//        User homer = new User("homer@simpson.com", "password".toCharArray(), "Homer Simpson", "Rh.D.", "United Kingdom");
//        User bart = new User("bart@simpson.com", "password".toCharArray(), "Bart Simpson", "Ph.D.", "United States");
//        homer.addFriend(bart);
//        bart.acceptFriendRequest(homer);
//        Chat chat = homer.getDirectChat(bart.getId()).get();
//        chat.addMember(lisa);
//        ChatRepository.findAll().forEach(System.out::println);
//        homer.createGroupChat("homer and bart", Set.of(bart.getId()));
//
//        Optional<Chat> chat = ChatRepository.findByName("homer and bart").stream().findFirst();
//        if (chat.isPresent()) {
//
//            homer.sendMessage(chat.get(), new TextContent("This is a message of Homer"), null);
//            homer.viewChat(chat.get());
//        }
//        homer.createMedicalcase("Test");
//        Medicalcase medicalcase = MedicalcaseRepository.findAll().stream().findFirst().get();
//        medicalcase.addVotingOption("aids");
//        medicalcase.addVotingOption("cancer");
//        medicalcase.addVotingOption("idk");
//        medicalcase.publish();
//        medicalcase.addMember(bart);
//        medicalcase.addMember(lisa);
//        medicalcase.castVote(lisa, "aids", 20);
//        medicalcase.castVote(lisa, "cancer", 50);
//        medicalcase.castVote(bart, "aids", 0);
//        medicalcase.castVote(bart, "cancer", 90);
//        medicalcase.viewVotes();
//        bart.sendMessage(medicalcase.getChat(), "bart message in medicalcase", null);
//        bart.sendMessage(medicalcase.getChat(), "bart message in medicalcase", null);
//        medicalcase.viewChat();
//    }
}
