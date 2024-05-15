package domain.Messenger;

import domain.common.BaseEntity;
import domain.common.Media;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Chat extends BaseEntity {

    // not null, not blank, max 64 characters
    private String name;
    // not null, max 512 members, not empty
    private Set<UUID> members;
    // not null
    private List<Message> history;

    public void sendMessage(String content, List<Media> attachments){

    }

    public void reply(Message message){

    }

    public void view(){

    }
}
