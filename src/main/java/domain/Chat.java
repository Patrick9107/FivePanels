package domain;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Chat extends BaseEntity {

    private String name;
    private Set<UUID> members;
    private List<Message> history;

    public void sendMessage(String content, List<Media> attachments){

    }

    public void reply(Message message){

    }

    public void view(){

    }
}
