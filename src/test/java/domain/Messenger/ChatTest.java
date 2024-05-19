package domain.Messenger;

import domain.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChatTest {

    private User homer;
    private User bart;

    @BeforeEach
    void setup() {
        homer = new User("homer@simpson.com", "password", "Homer Simpson", "Rh.D.", "United Kingdom");
        bart = new User("bart@simpson.com", "password", "Bart Simpson", "Ph.D.", "United States");
    }

    @Test
    void test() {

    }
}
