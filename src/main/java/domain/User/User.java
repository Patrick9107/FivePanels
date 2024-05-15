package domain.User;

import domain.common.BaseEntity;
import domain.Messenger.Chat;
import domain.Medicalcase.Medicalcase;

import java.util.Set;

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
    // not null
    private Set<Medicalcase> partOfMedicalcase; // Diese Variable vllt noch ändern weil man ja owner oder member sein kann. Also eventuell eine Map. Und der name der Variable ist auch nicht elegant

    public void verify() {
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

}