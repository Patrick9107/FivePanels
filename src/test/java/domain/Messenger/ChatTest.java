package domain.Messenger;

import domain.User.User;
import foundation.AssertException;
import org.junit.jupiter.api.*;
import repository.ChatRepository;
import repository.UserRepository;

import java.util.HashSet;
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
        if (chat != null)
            ChatRepository.deleteById(chat.getId());
        if (homer != null)
            UserRepository.deleteById(homer.getId());
        if (bart != null)
            UserRepository.deleteById(bart.getId());
        if (lisa != null)
            UserRepository.deleteById(lisa.getId());
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
            homer.addFriend(bart);
            bart.acceptFriendRequest(homer);
            // Then
            assertEquals(2, ChatRepository.count());
            Chat chat;
            if (homer.getDirectChat(bart.getId()).isPresent()) {
                chat = homer.getDirectChat(bart.getId()).get();
            } else {
                chat = null;
            }
            assertNotNull(chat);
            Chat newGroupChat = chat.addMember(lisa);
            assertEquals(3, ChatRepository.count());
            assertEquals(3, newGroupChat.getMembers().size());
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
}
