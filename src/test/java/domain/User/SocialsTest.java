package domain.User;

import foundation.AssertException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.ChatRepository;
import repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

class SocialsTest {

    private User homer;
    private User bart;

    @BeforeEach
    void setup() {
        // Given
        // TODO Tests brauchen viel zu lange weil das Passwort Verfahren solange dauert
        //  -> Lösung: Bevor man testet cost bei hashpassword in password klasse von 12 auf 4 (minimum) machen
        homer = new User("homer@simpson.com", "spengergasse".toCharArray(), "Homer Simpson", "Rh.D.", "United Kingdom");
        bart = new User("bart@simpson.com", "spengergasse".toCharArray(), "Bart Simpson", "Ph.D.", "United States");
    }

    @AfterEach
    void deleteFromRepo() {
        UserRepository.findAll().forEach(user -> UserRepository.deleteById(user.getId()));
    }

    @Test
    void addFriend_shouldWork_WhenNoRelationExists() {
        try {
            // When
            assertFalse(homer.getSocials().getRelation().containsKey(bart.getId()));
            assertFalse(bart.getSocials().getRelation().containsKey(homer.getId()));
            // Then
            homer.addFriend(bart);
            assertEquals(Relation.OUTGOING, homer.getSocials().getRelation().get(bart.getId()));
            assertEquals(Relation.INCOMING, bart.getSocials().getRelation().get(homer.getId()));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void addFriend_shouldThrow_WhenUserTriesToAddHimself() {
        try {
            // When
            // Then
            assertThrowsExactly(UserException.class, () -> homer.addFriend(homer));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void addFriend_shouldThrow_WhenUserTriesToAddTheSameUserTwice() {
        try {
            // When
            homer.addFriend(bart);
            // Then
            assertThrowsExactly(UserException.class, () -> homer.addFriend(bart));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void addFriend_shouldThrow_WhenUserTriesToAddNull() {
        try {
            // When
            // Then
            assertThrowsExactly(AssertException.class, () -> homer.addFriend(null));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void addFriend_shouldThrow_WhenUserIsAlreadyFriendsWithOtherUser() {
        try {
            // When
            homer.addFriend(bart);
            bart.acceptFriendRequest(homer);
            assertEquals(Relation.FRIENDS, homer.getSocials().getRelation().get(bart.getId()));
            assertEquals(Relation.FRIENDS, bart.getSocials().getRelation().get(homer.getId()));
            // Then
            assertThrowsExactly(UserException.class, () -> homer.addFriend(bart));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void addFriend_shouldEstablishFriendship_WhenOtherUserAlreadyAddedUser() {
        try {
            // When
            bart.addFriend(homer);
            homer.addFriend(bart);
            // Then
            assertEquals(Relation.FRIENDS, homer.getSocials().getRelation().get(bart.getId()));
            assertEquals(Relation.FRIENDS, bart.getSocials().getRelation().get(homer.getId()));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void acceptFriendRequest_shouldEstablishFriendship_WhenHavingIncomingRelation() {
        try {
            // When
            homer.addFriend(bart);
            // Then
            bart.acceptFriendRequest(homer);
            assertEquals(Relation.FRIENDS, homer.getSocials().getRelation().get(bart.getId()));
            assertEquals(Relation.FRIENDS, bart.getSocials().getRelation().get(homer.getId()));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void acceptFriendRequest_shouldThrow_WhenUserTriesToAcceptRequestFromHimself() {
        try {
            // When
            // Then
            assertThrowsExactly(UserException.class, () -> homer.acceptFriendRequest(homer));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void acceptFriendRequest_shouldThrow_WhenAcceptingRequestFromNull() {
        try {
            // When
            // Then
            assertThrowsExactly(AssertException.class, () -> homer.acceptFriendRequest(null));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void acceptFriendRequest_shouldThrow_WhenHavingOutgoingRelation() {
        try {
            // When
            bart.addFriend(homer);
            // Then
            assertThrowsExactly(UserException.class, () -> bart.acceptFriendRequest(homer));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void acceptFriendRequest_shouldThrow_WhenTryingToAcceptRequestFromFriend() {
        try {
            // When
            bart.addFriend(homer);
            homer.acceptFriendRequest(bart);
            // Then
            assertThrowsExactly(UserException.class, () -> bart.acceptFriendRequest(homer));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void acceptFriendRequest_shouldThrow_WhenTryingToAcceptFromUserWithNoRelation() {
        try {
            // When
            // Then
            assertThrowsExactly(UserException.class, () -> bart.acceptFriendRequest(homer));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void denyFriendRequest_shouldRemoveUserAsRelation_WhenHavingIncomingRelation() {
        try {
            // When
            homer.addFriend(bart);
            // Then
            bart.denyFriendRequest(homer);
            assertFalse(homer.getSocials().getRelation().containsKey(bart.getId()));
            assertFalse(bart.getSocials().getRelation().containsKey(homer.getId()));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void denyFriendRequest_shouldThrow_WhenUserTriesToDenyRequestFromHimself() {
        try {
            // When
            // Then
            assertThrowsExactly(UserException.class, () -> homer.denyFriendRequest(homer));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void denyFriendRequest_shouldThrow_WhenDenyingRequestFromNull() {
        try {
            // When
            // Then
            assertThrowsExactly(AssertException.class, () -> homer.denyFriendRequest(null));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void denyFriendRequest_shouldThrow_WhenHavingOutgoingRelation() {
        try {
            // When
            bart.addFriend(homer);
            // Then
            assertThrowsExactly(UserException.class, () -> bart.denyFriendRequest(homer));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void denyFriendRequest_shouldThrow_WhenTryingToDenyRequestFromFriend() {
        try {
            // When
            bart.addFriend(homer);
            homer.acceptFriendRequest(bart);
            // Then
            assertThrowsExactly(UserException.class, () -> bart.denyFriendRequest(homer));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void denyFriendRequest_shouldThrow_WhenTryingToDenyFromUserWithoutRelation() {
        try {
            // When
            // Then
            assertThrowsExactly(UserException.class, () -> bart.denyFriendRequest(homer));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void removeFriend_shouldRemoveUserAsRelation_WhenUserIsFriendsWithOtherUser() {
        try {
            // When
            homer.addFriend(bart);
            bart.acceptFriendRequest(homer);
            homer.removeFriend(bart);
            // Then
            assertFalse(homer.getSocials().getRelation().containsKey(bart.getId()));
            assertFalse(bart.getSocials().getRelation().containsKey(homer.getId()));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void removeFriend_shouldThrow_WhenUserTriesToRemoveHimselfAsFriend() {
        try {
            // When
            // Then
            assertThrowsExactly(UserException.class, () -> homer.removeFriend(homer));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void removeFriend_shouldThrow_WhenTryingToRemoveNull() {
        try {
            // When
            // Then
            assertThrowsExactly(AssertException.class, () -> homer.removeFriend(null));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void removeFriend_shouldThrow_WhenHavingOutgoingRelation() {
        try {
            // When
            bart.addFriend(homer);
            // Then
            assertThrowsExactly(UserException.class, () -> bart.removeFriend(homer));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void removeFriend_shouldThrow_WhenHavingIncomingRelation() {
        try {
            // When
            bart.addFriend(homer);
            // Then
            assertThrowsExactly(UserException.class, () -> homer.removeFriend(bart));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
    @Test
    void removeFriend_shouldThrow_WhenTryingToRemoveUserWithoutRelation() {
        try {
            // When
            // Then
            assertThrowsExactly(UserException.class, () -> bart.denyFriendRequest(homer));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
}