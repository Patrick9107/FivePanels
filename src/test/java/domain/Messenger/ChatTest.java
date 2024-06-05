package domain.Messenger;

import domain.User.User;
import org.junit.jupiter.api.*;
import repository.ChatRepository;
import repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

public class ChatTest {

    private User homer;
    private User bart;
    private User lisa;

    @BeforeEach
    void setup() {
        homer = new User("homer@simpson.com", "spengergasse".toCharArray(), "Homer Simpson", "Rh.D.", "United Kingdom");
        bart = new User("bart@simpson.com", "spengergasse".toCharArray(), "Bart Simpson", "Ph.D.", "United States");
        lisa = new User("lisa@simpson.com", "spengergasse".toCharArray(), "Lisa Simpson", "Ph.D.", "United States");
    }

    @Test
    void addMember_shouldCreateNewGroupChat_WhenTryingToAddMemberToDirectChat() {
        try {
            // When
            homer.addFriend(bart);
            bart.acceptFriendRequest(homer);
            // Then
            assertEquals(1, ChatRepository.findAll().size());
            Chat chat = null;
            if (homer.getDirectChat(bart.getId()).isPresent()) {
                chat = homer.getDirectChat(bart.getId()).get();
            }
            assert chat != null;
            chat.addMember(lisa);
            assertEquals(2, ChatRepository.findAll().size());
            homer.getDirectChat(bart.getId()).get().getMembers().forEach(uuid -> UserRepository.findById(uuid).ifPresent(user -> System.out.print(user.getProfile().getName())));
            System.out.println();
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
}
