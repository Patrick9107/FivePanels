package domain.User;

import domain.Medicalcase.Medicalcase;
import domain.Messenger.Chat;
import domain.Messenger.Status;
import domain.common.TextContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.ChatRepository;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User homer;
    private User bart;

    @BeforeEach
    void setup() {
        // Given
        homer = new User("homer@simpson.com", "password", "Homer Simpson", "Rh.D.", "United Kingdom");
        bart = new User("bart@simpson.com", "password", "Bart Simpson", "Ph.D.", "United States");
    }

    @Test
    void verify_shouldSetVerifiedTrue_WhenNotAlreadyVerified() {
        try {
            // When
            assertFalse(homer.isVerified());
            homer.verify();
            // Then
            assertTrue(homer.isVerified());
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void verify_shouldThrow_WhenAlreadyVerified() {
        try {
            // When
            assertFalse(homer.isVerified());
            homer.verify();
            // Then
            assertThrowsExactly(UserException.class, () -> homer.verify(), STR."verify(): user is already verified");
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void getDirectChat_shouldReturnOptionalContainingChat_WhenUserIsFriendsWithOtherUser() {
        try {
            // When
            homer.addFriend(bart);
            bart.acceptFriendRequest(homer);
            assertEquals(1, homer.getChats().size());
            assertEquals(1, bart.getChats().size());
            // Then
            assertEquals(homer.getChats().stream().findFirst().get(), homer.getDirectChat(bart.getId()).get());
            assertEquals(bart.getChats().stream().findFirst().get(), bart.getDirectChat(homer.getId()).get());
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void getDirectChat_shouldReturnEmptyOptional_WhenUserIsNotFriendsWithOtherUser() {
        try {
            // When
            assertEquals(0, homer.getChats().size());
            assertEquals(0, bart.getChats().size());
            // Then
            assertTrue(bart.getDirectChat(homer.getId()).isEmpty());
            assertTrue(homer.getDirectChat(bart.getId()).isEmpty());
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void getDirectChat_shouldReturnEmptyOptional_WhenUserIsNoLongerFriendsWithOtherUser() {
        try {
            // When
            homer.addFriend(bart);
            bart.acceptFriendRequest(homer);
            assertEquals(1, homer.getChats().size());
            assertEquals(1, bart.getChats().size());
            homer.removeFriend(bart);
            assertEquals(0, homer.getChats().size());
            assertEquals(0, bart.getChats().size());
            // Then
            assertTrue(bart.getDirectChat(homer.getId()).isEmpty());
            assertTrue(homer.getDirectChat(bart.getId()).isEmpty());
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }


    @Test
    void viewChat_shouldPrintChatDetailsAndChatName_WhenChatIsGroupChat() {
        try {
            // Given
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));
            homer.addFriend(bart);
            bart.acceptFriendRequest(homer);
            homer.createGroupChat("homer and bart", Set.of(bart.getId()));

            Optional<Chat> chat = ChatRepository.findByName("homer and bart").stream().findFirst();
            //When
            homer.sendMessage(chat.get(), new TextContent("This is a message of Homer"), null);
            homer.viewChat(chat.get());
            // Then
            assertTrue(outContent.toString().contains(homer.getProfile().getName()));
            assertTrue(outContent.toString().contains("homer and bart"));
            assertTrue(outContent.toString().contains("This is a message of Homer"));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void viewChat_shouldPrintOtherMemberNameAndChatDetails_WhenChatIsDirectChat() {
        try {
            // Given
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));
            homer.addFriend(bart);
            bart.acceptFriendRequest(homer);
            //When
            Chat chat = homer.getDirectChat(bart.getId()).get();
            // bart sends a message
            bart.sendMessage(chat, new TextContent("This is a message of Homer"), null);
            homer.viewChat(chat);
            // Then
            // homer views the chat, therefore he should see the other user's name (bart) as the chat name
            assertTrue(outContent.toString().contains(bart.getProfile().getName()));
            // homer should not see his own name because he did not send a message nor does bart view the chat (which should display homer's name)
            assertFalse(outContent.toString().contains(homer.getProfile().getName()));
            assertTrue(outContent.toString().contains("This is a message of Homer"));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void createMedicalcase_shouldAddMedicalcaseToOwner_WhenCalled() {
        try {
            // When
            // Then
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
}