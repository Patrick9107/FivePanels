package domain.User;

import domain.Messenger.Chat;
import repository.ChatRepository;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

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
        Chat chat = new Chat(null, Set.of(userToAccept.getId(), user.getId()), false);
        user.getChats().add(chat);
        userToAccept.getChats().add(chat);
        ChatRepository.save(chat);
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

    /**
     * Removes a friend from a user's friend list.
     * Throws an exception if the user tries to remove themselves or if they are not friends with the specified user.
     *
     * @param user         the user performing the removal
     * @param userToRemove the user to be removed from the friend list
     * @throws UserException if the user tries to remove themselves or if they are not friends with the specified user
     */
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
                    user.getChats().remove(chat);
                    userToRemove.getChats().remove(chat);
                    ChatRepository.deleteById(chat.getId());
                });
    }

    public List<UUID> listFriendRequests() {
        return relation.entrySet().stream().filter(uuidRelationEntry -> uuidRelationEntry.getValue().equals(Relation.INCOMING))
                                            .map(Map.Entry::getKey).toList();
    }
}
