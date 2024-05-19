package domain.Messenger;

import domain.User.User;
import domain.common.BaseEntity;
import domain.common.Media;
import domain.common.TextContent;
import foundation.Assert;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static foundation.Assert.*;

public class Chat extends BaseEntity {

    // not null, not blank, max 64 characters
    private String name;
    private boolean groupChat;
    // not null, max 512 members, not empty
    private Set<UUID> members;
    // not null
    private List<Message> history;

    public Chat(String name, Set<UUID> members) {
        setName(name);
        setMembers(members);
        history = new ArrayList<>();
    }

    public void setName(String name) {
        isNotNull(name, "name");
        hasMaxLength(name,513, "name");
        isNotBlank(name, "name");
        this.name = name;
    }

    public void setMembers(Set<UUID> members) {
        isNotNull(members, "members");
        hasMaxSize(members,513, "members");
        this.members = members;
    }

    public void addToHistory(Message message){
        isNotNull(message,"message");
        history.add(message);
    }

    public void sendMessage(User user, Chat chat, TextContent content, List<Media> attachments){
        isNotNull(chat, "chat");
        isNotNull(attachments, "attachments");
        isNotNull(content, "content");
        isNotNull(user, "user");

        if(!(members.contains(user.getId())))
            throw new MessengerException("sendMessage(): User is not a member of this chat");
        chat.addToHistory(new Message(user, Instant.now(), content, attachments, Status.SENT));
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

    public void reply(Message message){

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        history.forEach(sb::append);
        return sb.toString();
    }

// Test for sending Messages between 2 Users (it actually works) pls dont delete i need this code
//    public static void main(String[] args) {
//        User homer = new User("homer@simpson.com", "password", "Homer Simpson", "Rh.D.", "United Kingdom");
//        User bart = new User("bart@simpson.com", "password", "Bart Simpson", "Ph.D.", "United States");
//        homer.addFriend(bart);
//        bart.acceptFriendRequest(homer);
//
//        Optional<Chat> sendMessage = homer.getChats().stream().filter(chat -> !chat.isGroupChat() && chat.getMembers().contains(bart.getId())).findFirst();
//        sendMessage.ifPresent(chat -> homer.sendMessage(chat, new TextContent("Das ist Homer Message test test"), List.of(new Media("hallo.txt", "sdhjgvbfd", 100))));
//        sendMessage.ifPresent(chat -> bart.sendMessage(chat, new TextContent("Das ist Bart Message hallo 123 Test"), List.of(new Media("hallo.txt", "sdhjgvbfd", 100))));
//        sendMessage.ifPresent(homer::viewChat);
//        sendMessage.ifPresent(bart::viewChat);
//    }
}
