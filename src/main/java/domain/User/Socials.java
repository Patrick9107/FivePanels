package domain.User;

import domain.Messenger.Chat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static foundation.Assert.*;

public class Socials {

    // not null
    private Map<UUID, Relation> relation;

    public Socials() {
        this.relation = new HashMap<>();
    }

    public Map<UUID, Relation> getRelation() {
        return relation;
    }

    public void addFriend(User user, User userToAdd) {
        isNotNull(userToAdd, "userToAdd");
        isNotNull(user, "user");

        if (user.equals(userToAdd))
            throw new UserException(STR."addFriend(): User can not add himself");
        if (relation.get(userToAdd.getId()) == Relation.INCOMING) {
            acceptFriendRequest(user, userToAdd);
            return;
        }
        if (relation.containsKey(userToAdd.getId()))
            throw new UserException(STR."addFriend(): User already has a Relation with \{userToAdd}");
        relation.put(userToAdd.getId(), Relation.OUTGOING);
        userToAdd.getSocials().relation.put(user.getId(), Relation.INCOMING);
        Chat chat = new Chat(userToAdd.getProfile().getName(), Set.of(userToAdd.getId(), user.getId()), false); // TODO der chat heißt bei jedem gleich? auch bei dm
        user.addChat(chat);
        userToAdd.addChat(chat);
    }

    public void acceptFriendRequest(User user, User userToAccept){
        isNotNull(userToAccept, "userToAcceptRequest");
        isNotNull(user, "user");

        if (user.equals(userToAccept))
            throw new UserException(STR."acceptFriendRequest(): User can not accept a friend request from himself");
        if (!(relation.get(userToAccept.getId()) == Relation.INCOMING))
            throw new UserException(STR."acceptFriendRequest(): User does not have an incoming friend request from \{userToAccept}");
        relation.put(userToAccept.getId(), Relation.FRIENDS);
        userToAccept.getSocials().relation.put(user.getId(), Relation.FRIENDS);
    }

    public void denyFriendRequest(User user, User userToDeny){
        isNotNull(userToDeny, "userToDeny");
        isNotNull(user, "user");

        if (user.equals(userToDeny))
            throw new UserException(STR."denyFriendRequest(): User can not deny a friend request from himself");
        if (!(relation.get(userToDeny.getId()) == Relation.INCOMING))
            throw new UserException(STR."denyFriendRequest(): User does not have an incoming friend request from \{userToDeny}");
        relation.remove(userToDeny.getId());
        userToDeny.getSocials().relation.remove(user.getId());
    }

    public void removeFriend(User user, User userToRemove) {
        isNotNull(user, "user");
        isNotNull(userToRemove, "userToRemove");

        if (user.equals(userToRemove))
            throw new UserException(STR."removeFriend(): User can not remove himself");
        if (!(relation.get(userToRemove.getId()) == Relation.FRIENDS))
            throw new UserException(STR."removeFriend(): User is not friends with \{userToRemove}");
        relation.remove(userToRemove.getId());
        userToRemove.getSocials().getRelation().remove(user.getId());

        // Deletes the chat between the 2 Users
        user.getChats().stream().filter(chat ->
                chat.getMembers().contains(userToRemove.getId()) && !chat.isGroupChat()).forEach(chat -> {
                    user.removeChat(chat);
                    userToRemove.removeChat(chat);
                });
    }
}
