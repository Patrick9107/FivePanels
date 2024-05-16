package domain.User;

import domain.Messenger.Message;
import domain.Messenger.MessengerException;
import domain.Messenger.Status;
import domain.common.BaseEntity;
import domain.Messenger.Chat;
import domain.Medicalcase.Medicalcase;
import domain.common.Media;
import domain.common.TextContent;
import foundation.Assert;

import java.time.Instant;
import java.util.*;

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
    private /*LinkedHash*/Set<Chat> chats;
    // not null, exactly 2 entries
    private Map<Ownership, Set<Medicalcase>> medicalcases;

    // TODO NUR ZUM TESTEN, KANN WIEDER GELÖSCHT WERDEN
    public User() {
        this.email = new Email("admin@admin.com");
        this.hashedPassword = new Password("admin".toCharArray());
        this.profile = new Profile();
        this.socials = new Socials();
        verified = false;
        chats = new LinkedHashSet<>();
        setMedicalcases();
    }

    public void verify() {
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setMedicalcases() {
        medicalcases = new HashMap<>();
        medicalcases.put(Ownership.OWNER, new HashSet<>());
        medicalcases.put(Ownership.MEMBER, new HashSet<>());
    }

    public void sendMessage(Chat chat, TextContent content, List<Media> attachments){
        Assert.isNotNull(chat, "chat");
        Assert.isNotNull(attachments, "attachments");
        Assert.isNotNull(content, "content");

        if(chats.contains(chat)) {
            chat.addToHistory(new Message(this, Instant.now(), content, attachments, Status.SENT));
        } else {
            throw new MessengerException("sendMessage: chat doesnt exist");
        }
    }
}