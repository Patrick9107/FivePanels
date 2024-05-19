package domain.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SocialsTest {

    private User homer;
    private User bart;

    @BeforeEach
    void setup() {

        homer = new User("homer@simpson.com", "password", "Homer Simpson", "Rh.D.", "United Kingdom");
        bart = new User("bart@simpson.com", "password", "Bart Simpson", "Ph.D.", "United States");
    }

    @Test
    void addFriend_shouldThrow_WhenOneUserAddedAnotherAlready() {

        //Given
        homer.getSocials().addFriend(homer, bart);

        //When

    }
//    @Test
//    void SUT_whenXXX_thenXXX() {
//
//    }
//    @Test
//    void SUT_whenXXX_thenXXX() {
//
//    }
//    @Test
//    void SUT_whenXXX_thenXXX() {
//
//    }
//    @Test
//    void SUT_whenXXX_thenXXX() {
//
//    }
//    @Test
//    void SUT_whenXXX_thenXXX() {
//
//    }
//    @Test
//    void SUT_whenXXX_thenXXX() {
//
//    }
//    @Test
//    void SUT_whenXXX_thenXXX() {
//
//    }
}