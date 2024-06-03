package domain.User;

import domain.Medicalcase.Answer;
import domain.Medicalcase.MedicalcaseException;
import domain.Messenger.MessengerException;
import domain.common.BaseEntity;
import domain.Messenger.Chat;
import domain.Medicalcase.Medicalcase;
import domain.common.Content;
import domain.common.Media;
import domain.common.TextContent;
import repository.ChatRepository;
import repository.UserRepository;

import java.util.*;

import static foundation.Assert.*;

/**
 * Represents a user in the system.
 * This class extends {@link BaseEntity} and includes user-specific properties and methods.
 * It provides functionalities such as managing friend requests, chats, and medicalcases
 */
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

    /**
     * Constructs a new {@link User} instance with the specified email, password, name, title, and location.
     * Also initializes the user's socials, chats, and medicalcases.
     *
     * @param email the user's email address
     * @param password the user's password
     * @param name the user's name
     * @param title the user's title
     * @param location the user's location
     */
    public User(String email, String password, String name, String title, String location) {
        this.email = new Email(email);
        this.hashedPassword = new Password(password);
        this.profile = new Profile(name, title, location);
        this.socials = new Socials();
        this.chats = new LinkedHashSet<>();
        setMedicalcases();
        UserRepository.save(this);
    }

    /**
     * Verifies the user.
     * If the user is already verified, this method throws a {@link UserException}.
     * Otherwise, it sets the user's verified status to true.
     *
     * @throws UserException if the user is already verified
     */
    public void verify() {
        if (verified)
            throw new UserException(STR."verify(): user is already verified");
        this.verified = true;
    }

    /**
     * This method sets up the {@code medicalcases} HashMap with keys for {@link Ownership#OWNER} and
     * {@link Ownership#MEMBER}, each associated with an empty {@link HashSet} of medicalcases.
     */
    public void setMedicalcases() {
        medicalcases = new HashMap<>();
        medicalcases.put(Ownership.OWNER, new HashSet<>());
        medicalcases.put(Ownership.MEMBER, new HashSet<>());
    }

    /**
     * Returns a {@link Set} of chats where the user is a part of
     * @return a {@link Set} of chats
     */
    public Set<Chat> getChats() {
        return chats;
    }

    /**
     * Creates a new group chat with the specified name and members.
     *
     * @param name the name of the group chat
     * @param members a set of UUIDs representing the members to be added to the group chat
     * @return the chat that has been created
     */
    public Chat createGroupChat(String name, Set<UUID> members) {
        isNotBlank(name, "name");
        isNotEmpty(members, "members");
        HashSet<UUID> set = new HashSet<>(members);
        set.add(getId());
        Chat chat = new Chat(name, set,true);
        members.forEach(uuid -> UserRepository.findById(uuid).ifPresent(user -> user.getChats().add(chat)));
        return chat;
    }

    /**
     * Sends a message to the specified chat.
     * This method calls the {@code sendMessage} method on the given chat.
     *
     * @param chat the chat to which the message is to be sent
     * @param content the text content of the message
     * @param attachments a list of media attachments to be included with the message
     */
    public void sendMessage(Chat chat, String content, List<Media> attachments) {
        chat.sendMessage(this, chat, new TextContent(content), attachments);
    }

    /**
     * Displays information about a chat.
     * If the chat is not a group chat, it prints the name of the other member.
     * If the chat is a group chat, it prints the chat's name.
     * It also prints the chat's history.
     *
     * @param chat the chat to be viewed
     * @throws MessengerException if the user is not a member of specified chat
     */
    public void viewChat(Chat chat) {
        if (!(chat.getMembers().contains(this.getId())))
            throw new MessengerException(STR."viewChat(): user is not part of this chat");
        if (!(chat.isGroupChat())) {
            chat.getMembers().stream().filter(uuid ->
                    this.getId() != uuid).findFirst().flatMap(UserRepository::findById).ifPresent(user ->
                    System.out.println(user.getProfile().getName()));
        } else {
            System.out.println(chat.getName());
        }
        chat.getHistory().forEach(System.out::println);
    }

    /**
     * Displays information about a chat.
     * If a chat with the specified ID is found, it calls {@link #viewChat(Chat)} to display the chat's details.
     *
     * @param chatId the UUID of the chat to be viewed
     */
    public void viewChat(UUID chatId) {
        ChatRepository.findById(chatId).ifPresent(this::viewChat);
    }

    /**
     * Retrieves a direct chat involving a specific user identified by their userId.
     * This method filters the list of chats to find a non-group chat where the
     * specified user as the other member. It returns an {@link Optional} containing the
     * direct chat if it exists, or an empty {@link Optional} if no such chat is found.
     *
     * @param userId the UUID of the user whose direct chat is to be retrieved
     * @return an {@link Optional} containing the direct chat if found, or an empty {@link Optional} if not
     */
    public Optional<Chat> getDirectChat(UUID userId) {
        return chats.stream().filter(chat -> !chat.isGroupChat() && chat.getMembers().contains(userId)).findFirst();
    }

    /**
     * Returns the {@link Socials} of the user
     * @return the {@link Socials} of the user
     */
    public Socials getSocials() {
        return socials;
    }

    /**
     * Returns the {@link Profile} of the user
     * @return the {@link Profile} of the user
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Returns the {@link Email} of the user
     * @return the {@link Email} of the user
     */
    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return hashedPassword;
    }

    /**
     * Returns a map of the user's medicalcases categorized by ownership.
     *
     * @return a {@link Map} where the keys are {@link Ownership} and the values are {@link Set} of {@link Medicalcase}
     */
    public Map<Ownership, Set<Medicalcase>> getMedicalcases() {
        return medicalcases;
    }

    /**
     * Checks if the user is verified.
     *
     * @return {@code true} if the user is verified, {@code false} otherwise
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * Sends a friend request to the specified user.
     * This method calls the {@code addFriend} method in the {@link Socials} class, passing the
     * current user instance (this) and the user to whom the friend request is to be sent.
     *
     * @param user the user to whom the friend request is to be sent
     */
    public void addFriend(User user) {
        this.socials.addFriend(this, user);
    }

    /**
     * Accepts a friend request for the current user.
     * This method calls the {@code acceptFriendRequest} method in the {@link Socials} class, passing the
     * current user instance (this) and the user whose friend request is being accepted.
     *
     * @param user the user whose friend request is to be accepted
     */
    public void acceptFriendRequest(User user) {
        this.socials.acceptFriendRequest(this, user);
    }

    /**
     * Denies a friend request for the current user.
     * This method calls the {@code denyFriendRequest} method in the {@link Socials} class, passing the
     * current user instance (this) and the user whose friend request is being denied.
     *
     * @param user the user whose friend request is to be denied
     */
    public void denyFriendRequest(User user) {
        this.socials.denyFriendRequest(this, user);
    }

    /**
     * Removes a friend from the current user's friend list.
     * This method calls the {@code removeFriend} method in the {@link Socials} class, passing the
     * current user instance (this) and the user to be removed as a friend.
     *
     * @param user the user to be removed from the friend list
     */
    public void removeFriend(User user) {
        this.socials.removeFriend(this, user);
    }

    /**
     * Creates a new medicalcase with the specified title and tags.
     * This method adds a new {@link Medicalcase} instance to the list of medicalcases. The current user is the owner of that medicalcase
     *
     * @param title the title of the medical case
     * @param tags  an optional list of tags associated with the medical case
     */
    public Medicalcase createMedicalcase(String title, String... tags) {
        Medicalcase medicalcase = new Medicalcase(title, this, tags);
        medicalcases.get(Ownership.OWNER).add(medicalcase);
        return medicalcase;
    }

    // Medicalcase methods ----------------------------------------------------


    public void castVote(Medicalcase medicalcase, String answer, int percentage) {
        if (!(medicalcases.get(Ownership.MEMBER).contains(medicalcase)))
            throw new MedicalcaseException(STR."castVote(): user is not a member of this medicalcase");
        medicalcase.castVote(this, answer, percentage);
    }

    public void removeContent(Medicalcase medicalcase, int index) {
        isOwner(medicalcase);
        medicalcase.removeContent(index);
    }

    public void removeContent(Medicalcase medicalcase, Content content) {
        isOwner(medicalcase);
        medicalcase.removeContent(content);
    }

    public void addContent(Medicalcase medicalcase, Content content, int index) {
        isOwner(medicalcase);
        medicalcase.addContent(content, index);

    }

    public void addContent(Medicalcase medicalcase, Content content) {
        isOwner(medicalcase);
        medicalcase.addContent(content);
    }

    public void removeVotingOption(Medicalcase medicalcase, Answer option) {
        isOwner(medicalcase);
        medicalcase.removeVotingOption(option);
    }

    public void addVotingOption(Medicalcase medicalcase, String option) {
        isOwner(medicalcase);
        medicalcase.addVotingOption(option);
    }

    public void publish(Medicalcase medicalcase) {
        isOwner(medicalcase);
        medicalcase.publish();
    }

    public void react(Medicalcase medicalcase) {
        if (!(medicalcases.get(Ownership.MEMBER).contains(medicalcase)))
            throw new MedicalcaseException(STR."react(): user is not a member of this medicalcase");
        medicalcase.react(this);
    }

    public void addTag(Medicalcase medicalcase, String tag) {
        isOwner(medicalcase);
        medicalcase.addTag(tag);
    }

    public void setCorrectAnswer(Medicalcase medicalcase, Answer answer) {
        isOwner(medicalcase);
        medicalcase.setCorrectAnswer(answer);
    }

    public void setTags(Medicalcase medicalcase, String... tags) {
        isOwner(medicalcase);
        medicalcase.setTags(tags);
    }

    public void setTitle(Medicalcase medicalcase, String title) {
        isOwner(medicalcase);
        medicalcase.setTitle(title);
    }

    public void viewVotes(Medicalcase medicalcase) {
        isOwner(medicalcase);
        medicalcase.viewVotes();
    }

    public void addMember(Medicalcase medicalcase, User user) {
        isOwner(medicalcase);
        medicalcase.addMember(user);
    }

    private void isOwner(Medicalcase medicalcase) {
        if (!(medicalcase.getOwner().equals(this)))
            throw new MedicalcaseException(STR."action failed: user is not the owner of the medicalcase");
    }
}
