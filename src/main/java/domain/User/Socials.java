package domain.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static foundation.Assert.*;

public class Socials {

    // not null
    private Map<UUID, Relation> relation;

    public Socials() {
        this.relation = new HashMap<>();
    }

    //                       this
    public void addFriend(User user, User userToAdd) {
        isNotNull(userToAdd, "userToAdd");
        isNotNull(user, "user");

        if (!(relation.containsKey(userToAdd.getId())))
            throw new UserException("add(): User already exists in Map");
        relation.put(userToAdd.getId(), Relation.OUTGOING);
        userToAdd.getSocials().relation.put(user.getId(), Relation.INCOMING);
    }

    //                                 this
    public void acceptFriendRequest(User user, User userToAccept){
        isNotNull(userToAccept, "userToAcceptRequest");
        isNotNull(user, "user");

        if (!(relation.get(userToAccept.getId()) == Relation.INCOMING))
            throw new UserException(STR."acceptFriendRequest(): User does not have an incoming friend request from \{userToAccept}");
        relation.put(userToAccept.getId(), Relation.FRIENDS);
        userToAccept.getSocials().relation.put(user.getId(), Relation.FRIENDS);
    }

    //                               this
    public void denyFriendRequest(User user, User userToDeny){
        isNotNull(userToDeny, "userToDeny");
        isNotNull(user, "user");

        if (!(relation.get(userToDeny.getId()) == Relation.INCOMING))
            throw new UserException(STR."denyFriendRequest(): User does not have an incoming friend request from \{userToDeny}");
        relation.remove(userToDeny.getId());
        userToDeny.getSocials().relation.remove(user.getId());
    }

}
