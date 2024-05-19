package domain.User;

import domain.Messenger.Message;
import domain.Messenger.MessengerException;
import domain.Messenger.Status;
import domain.common.BaseEntity;
import domain.Messenger.Chat;
import domain.Medicalcase.Medicalcase;
import domain.common.Image;
import domain.common.Media;
import domain.common.TextContent;

import java.time.Instant;
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
    public User() {
        this.email = new Email("admin@admin.com");
        this.hashedPassword = new Password("admin".toCharArray());
        this.profile = new Profile();
        this.socials = new Socials();
        verified = false;
        chats = new LinkedHashSet<>();
        setMedicalcases();
    }

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

    public void sendMessage(Chat chat, TextContent content, List<Media> attachments){
        isNotNull(chat, "chat");
        isNotNull(attachments, "attachments");
        isNotNull(content, "content");

        if(!(chats.contains(chat)))
            throw new MessengerException("sendMessage(): chat does not exist");
        chat.addToHistory(new Message(this, Instant.now(), content, attachments, Status.SENT));
    }

    public Socials getSocials() {
        return socials;
    }
}