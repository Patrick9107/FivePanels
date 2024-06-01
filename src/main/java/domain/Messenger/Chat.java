package domain.Messenger;

import domain.User.User;
import domain.common.BaseEntity;
import domain.common.Media;
import domain.common.TextContent;
import repository.ChatRepository;
import repository.UserRepository;

import java.time.Instant;
import java.util.*;

import static foundation.Assert.*;

public class Chat extends BaseEntity {

    // can be null if groupchat false - not null otherwise, not blank, max 64 characters
    private String name;
    private boolean groupChat;
    // not null, max 512 members, not empty
    private Set<UUID> members;
    // not null
    private List<Message> history;

    public Chat(String name, Set<UUID> members, boolean groupChat) {
        this.groupChat = groupChat;
        setName(name);
        setMembers(members);
        history = new ArrayList<>();
    }

    public void setName(String name) {
        if (groupChat) {
            isNotNull(name, "name");
            hasMaxLength(name, 513, "name");
            isNotBlank(name, "name");
            this.name = name;
        }
    }

    public void setMembers(Set<UUID> members) {
        isNotNull(members, "members");
        hasMaxSize(members,513, "members");
        this.members = members;
    }

    public void addMember(User user) {
        isNotNull(user, "user");
        hasMaxSize(members, 513, "members");

        if (!groupChat) {
            // todo somehow get name from user based on id
            String groupName = members.stream().map(UUID::toString).toString() + user.getProfile().getName();
            Chat chat = new Chat(groupName, new HashSet<UUID>(members), true);
            user.addChat(chat);
            // todo other members also have to add the chat (again get user from uuid)
        }
        if (members.contains(user.getId()))
            throw new MessengerException(STR."addMember(): user is already a member of this chat");
        members.add(user.getId());
    }

    public void removeMember(User user) {
        if (!groupChat)
            throw new MessengerException(STR."removeMember(): can not remove a member from a direct chat");
        isNotNull(user, "user");
        hasMaxSize(members, 513, "members");

        if (!(members.contains(user.getId())))
            throw new MessengerException(STR."removeMember(): user is not a member of this chat");
        members.remove(user.getId());
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

    //todo vielleicht viewChat methode?

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
//        User homer = new User("homer@simpson.com", "password", "Homer Simpson", "Rh.D.", "United Kingdom");
//        UserRepository.save(homer);
//        User bart = new User("bart@simpson.com", "password", "Bart Simpson", "Ph.D.", "United States");
//        UserRepository.save(bart);
//        User lisa = new User("lisa@simpson.com", "password", "Lisa Simpson", "Ph.D.", "United States");
//        UserRepository.save(lisa);
//        homer.addFriend(bart);
//        bart.acceptFriendRequest(homer);
//
//        lisa.addChat(new Chat("theCoolOnes", Set.of(bart.getId(), lisa.getId(), homer.getId()), true));
//
//        Chat chat = homer.getDirectChat(bart.getId());
//        homer.sendMessage(chat, new TextContent("Das ist Homer Message test test"), List.of(new Media("hallo.txt", "sdhjgvbfd", 100)));
//        bart.sendMessage(chat, new TextContent("Das ist Bart Message hallo 123 Test"), List.of(new Media("hallo.txt", "sdhjgvbfd", 100)));
//        bart.viewChat(chat);
//    }
}
