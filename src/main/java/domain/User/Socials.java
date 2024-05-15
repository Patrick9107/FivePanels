package domain.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Socials {

    // not null
    private Map<UUID, Relation> relation;

    public Socials() {
        this.relation = new HashMap<>();
    }

    public User add(User user){
        return null;
    }

    public User accept(User user){
        return null;
    }

    public User deny(User user){
        return null;
    }

}
