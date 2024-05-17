package domain.Messenger;

import domain.common.BaseEntity;
import foundation.Assert;

import java.util.*;

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
        Assert.isNotNull(name, "name");
        Assert.hasMaxLength(name,513, "name");
        Assert.isNotBlank(name, "name");
        this.name = name;
    }

    public void setMembers(Set<UUID> members) {
        Assert.isNotNull(members, "members");
        Assert.hasMaxSize(members,513, "members");
        Assert.isNotBlank(name, "name");
        this.members = members;
    }

    public void addToHistory(Message message){
        Assert.isNotNull(message,"message");
        history.add(message);
    }


    public void reply(Message message){

    }

    public void view(){

    }
}
