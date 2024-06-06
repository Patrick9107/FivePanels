package domain.Messenger;

import domain.User.User;
import foundation.AssertException;
import org.junit.jupiter.api.*;
import repository.ChatRepository;
import repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ChatTest {

    private User homer;
    private User bart;
    private User lisa;

    private Chat chat;

    @BeforeEach
    void setup() {
        homer = new User("homer@simpson.com", "spengergasse".toCharArray(), "Homer Simpson", "Rh.D.", "United Kingdom");
        bart = new User("bart@simpson.com", "spengergasse".toCharArray(), "Bart Simpson", "Ph.D.", "United States");
        lisa = new User("lisa@simpson.com", "spengergasse".toCharArray(), "Lisa Simpson", "Ph.D.", "United States");
        chat = new Chat("Homer, Bart & Lisa", Set.of(homer.getId(), bart.getId(), lisa.getId()), true);
    }

    @AfterEach
    void deleteFromRepo() {
        ChatRepository.findAll().forEach(chat1 -> ChatRepository.deleteById(chat1.getId()));
        UserRepository.findAll().forEach(user -> UserRepository.deleteById(user.getId()));
    }

    @Test
    void setMembers_shouldSetMembers_WhenPassingIdsOfUsers() {
        try {
            // When
            assertEquals(chat.getMembers(), Set.of(homer.getId(), bart.getId(), lisa.getId()));
            chat.setMembers(Set.of(homer.getId(), bart.getId()));
            assertEquals(chat.getMembers(), Set.of(homer.getId(), bart.getId()));
            // Then
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void setMembers_shouldThrow_WhenMembersIsNull() {
        try {
            // When
            assertThrowsExactly(AssertException.class, () -> chat.setMembers(null));
            // Then
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void setMembers_shouldThrow_WhenAMemberIsNull() {
        try {
            // When
            Set<UUID> set1 = new HashSet<>();
            set1.add(homer.getId());
            set1.add(null);

            Set<UUID> set2 = new HashSet<>();
            set2.add(null);

            assertEquals(2, set1.size());
            assertEquals(1, set2.size());
            assertThrowsExactly(AssertException.class, () -> chat.setMembers(set1));
            assertThrowsExactly(AssertException.class, () -> chat.setMembers(set2));
            // Then
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void addMember_shouldCreateNewGroupChat_WhenTryingToAddMemberToDirectChat() {
        try {
            // When
            assertEquals(1, ChatRepository.count());
            // homer has to have both as friends
            homer.addFriend(bart);
            bart.acceptFriendRequest(homer);

            homer.addFriend(lisa);
            lisa.acceptFriendRequest(homer);
            // Then
            assertEquals(3, ChatRepository.count());
            Chat chat;
            if (homer.getDirectChat(bart.getId()).isPresent()) {
                chat = homer.getDirectChat(bart.getId()).get();
            } else {
                chat = null;
            }
            assertNotNull(chat);
            Chat newGroupChat = homer.addChatMember(chat, lisa);
            assertEquals(4, ChatRepository.count());
            assertEquals(3, newGroupChat.getMembers().size());
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    // MEGA TEST, only execute if you would like to test EVERYTHING
    // this tests creates over 500 Users and takes a while
    // todo? to mark
//    @Test
//    void addMember_shouldThrow_WhenMembersExceed512Entries() {
//        try {
//            // When
//            assertEquals(1, ChatRepository.count());
//            homer.addFriend(bart);
//            bart.acceptFriendRequest(homer);
//            assertEquals(2, ChatRepository.count());
//            homer.addFriend(lisa);
//            lisa.acceptFriendRequest(homer);
//            assertEquals(3, ChatRepository.count());
//            Set<UUID> set = new HashSet<>();
//            set.add(bart.getId());
//            set.add(lisa.getId());
//            Chat chat2 = homer.createGroupChat("test", set);
//            assertEquals(4, ChatRepository.count());
//            assertEquals(chat2, ChatRepository.findByName("test").stream().findFirst().get());
//            // Then
//            Set<UUID> set2 = new HashSet<>();
//            for (int i = 0; i < 511; i++) {
//                User user = new User(i+"email@gmail.com", "spengergasse".toCharArray(), "Bernd", "Dr.", "Austria");
//                user.addFriend(homer);
//                homer.acceptFriendRequest(user);
//                set2.add(user.getId());
//            }
//            User user = new User("test@gmail.com", "spengergasse".toCharArray(), "Bernd", "Dr.", "Austria");
//            Chat megaChat = homer.createGroupChat("mega chat", set2);
//            User user2 = new User("email2@gmail.com", "spengergasse".toCharArray(), "Bernd", "Dr.", "Austria");
//            assertThrowsExactly(AssertException.class, () -> homer.addChatMember(megaChat, user2));
//        } catch (Exception e) {
//            System.out.println("Unexpected Exception: " + e.getMessage());
//            fail();
//        }
//    }

    @Test
    void addMember_shouldThrow_WhenNotEveryMemberIsFriendsWithCreatorOfGroupChat() {
        try {
            // When
            assertEquals(1, ChatRepository.count());
            homer.addFriend(bart);
            bart.acceptFriendRequest(homer);
            assertEquals(2, ChatRepository.count());
            Set<UUID> set = new HashSet<>();
            set.add(bart.getId());
            set.add(lisa.getId());
            assertThrowsExactly(MessengerException.class, () -> homer.createGroupChat("test", set));
            assertEquals(2, ChatRepository.count());
            // Then
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void addMember_shouldHaveEveryMemberInChatName_WhenCreatingNewGroupChatOutOfDirectChat() {
        try {
            // When
            assertEquals(1, ChatRepository.count());
            homer.addFriend(bart);
            bart.acceptFriendRequest(homer);
            assertEquals(2, ChatRepository.count());
            homer.addFriend(lisa);
            lisa.acceptFriendRequest(homer);
            assertEquals(3, ChatRepository.count());
            Chat chat = homer.getDirectChat(bart.getId()).get();
            Chat groupChat = homer.addChatMember(chat, lisa);
            assertEquals(4, ChatRepository.count());
            // Then
            groupChat.getMembers().forEach(uuid -> assertTrue(groupChat.getName().contains(UserRepository.findById(uuid).get().getProfile().getName())));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
}
