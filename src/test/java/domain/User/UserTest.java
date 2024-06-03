package domain.User;

import domain.Medicalcase.Medicalcase;
import domain.Medicalcase.MedicalcaseException;
import domain.Messenger.Chat;
import domain.Messenger.MessengerException;
import domain.Messenger.Status;
import domain.common.TextContent;
import foundation.AssertException;
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
            homer.sendMessage(chat.get(), "This is a message of Homer", null);
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
            bart.sendMessage(chat, "This is a message of bart", null);
            homer.viewChat(chat);
            // Then
            // homer views the chat, therefore he should see the other user's name (bart) as the chat name
            assertTrue(outContent.toString().contains(bart.getProfile().getName()));
            // homer should not see his own name because he did not send a message nor does bart view the chat (which should display homer's name)
            assertFalse(outContent.toString().contains(homer.getProfile().getName()));
            assertTrue(outContent.toString().contains("This is a message of bart"));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void viewChat_shouldThrow_WhenUserIsNotAMemberOfChat() {
        try {
            // Given
            homer.addFriend(bart);
            bart.acceptFriendRequest(homer);
            //When
            Chat chat = homer.createGroupChat("test", Set.of(bart.getId()));
            // bart sends a message
            bart.sendMessage(chat, "This is a message of bart", null);
            chat.removeMember(bart);
            assertThrowsExactly(MessengerException.class, () -> bart.viewChat(chat), STR."viewChat(): user is not part of this chat");
            // Then
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void createMedicalcase_shouldCreateMedicalcase_WhenTagsParameterIsEmpty() {
        try {
            // When
            Medicalcase medicalcase = homer.createMedicalcase("test");
            // Then
            // owner should be homer
            assertEquals(homer, medicalcase.getOwner());
            // the medicalcase should have 0 members
            assertTrue(medicalcase.getMembers().isEmpty());
            // homer should have his created medicalcase in his attribute
            assertTrue(homer.getMedicalcases().get(Ownership.OWNER).contains(medicalcase));
            // homer should not be a member of a medicalcase
            assertTrue(homer.getMedicalcases().get(Ownership.MEMBER).isEmpty());
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void createMedicalcase_shouldCreateMedicalcase_WhenTitleAndCorrectTagsArePassed() {
        try {
            // When
            Medicalcase medicalcase = homer.createMedicalcase("test", "Addiction Medicine", "Balneology");
            // Then
            // owner should be homer
            assertEquals(homer, medicalcase.getOwner());
            // the medicalcase should have 0 members
            assertTrue(medicalcase.getMembers().isEmpty());
            // homer should have his created medicalcase in his attribute
            assertTrue(homer.getMedicalcases().get(Ownership.OWNER).contains(medicalcase));
            // homer should not be a member of a medicalcase
            assertTrue(homer.getMedicalcases().get(Ownership.MEMBER).isEmpty());
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void createMedicalcase_shouldCreateMedicalcase_WhenCreatingMultipleCases() {
        try {
            // When
            // homer creates 2
            Medicalcase medicalcase = homer.createMedicalcase("test");
            Medicalcase medicalcase2 = homer.createMedicalcase("test2");
            // bart creates 1
            Medicalcase medicalcase3 = bart.createMedicalcase("test3");
            // Then
            // owner should be homer
            assertEquals(homer, medicalcase.getOwner());
            assertEquals(homer, medicalcase2.getOwner());
            assertEquals(bart, medicalcase3.getOwner());
            // the medicalcase should have 0 members
            assertTrue(medicalcase.getMembers().isEmpty());
            assertTrue(medicalcase2.getMembers().isEmpty());
            assertTrue(medicalcase3.getMembers().isEmpty());
            // homer should have his created medicalcase in his attribute
            assertTrue(homer.getMedicalcases().get(Ownership.OWNER).contains(medicalcase));
            assertTrue(homer.getMedicalcases().get(Ownership.OWNER).contains(medicalcase2));
            assertTrue(bart.getMedicalcases().get(Ownership.OWNER).contains(medicalcase3));
            // homer should not be a member of a medicalcase
            assertTrue(homer.getMedicalcases().get(Ownership.MEMBER).isEmpty());
            assertTrue(bart.getMedicalcases().get(Ownership.MEMBER).isEmpty());
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void createMedicalcase_shouldThrow_WhenTitleIsNull() {
        try {
            // When
            // Then
            assertThrowsExactly(AssertException.class, () -> homer.createMedicalcase(null), STR."title is null");
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void createMedicalcase_shouldThrow_WhenATagIsNull() {
        try {
            // When
            // Then
            assertThrowsExactly(AssertException.class, () -> homer.createMedicalcase("test", "Addiction Medicine", null), STR."tag is null");
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void createMedicalcase_shouldThrow_WhenTagsIsNull() {
        try {
            // When
            // Then
            assertThrowsExactly(AssertException.class, () -> homer.createMedicalcase("test", null), STR."tags is null");
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void createMedicalcase_shouldThrow_WhenTryingToAddNonExistentTag() {
        try {
            // When
            // Then
            assertThrowsExactly(MedicalcaseException.class, () -> homer.createMedicalcase("test", "foobar"), STR."setTag(): tag does not exist");
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
}