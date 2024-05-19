package domain.Messenger;

import domain.User.User;
import domain.common.BaseEntity;
import domain.common.Media;
import domain.common.TextContent;
import foundation.Assert;

import java.time.Instant;
import java.util.*;

import static foundation.Assert.*;

public class Chat extends BaseEntity {

    // not null, not blank, max 64 characters
    private String name;
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


    public void reply(Message message){

    }

    public void view(){
        history.forEach(System.out::println);
    }
}
