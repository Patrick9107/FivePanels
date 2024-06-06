package domain.Messenger;

import domain.User.User;
import domain.common.Media;
import domain.common.TextContent;
import foundation.Assert;
import foundation.AssertException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.ChatRepository;
import repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

class MessageTest {

    private User homer;
    private User bart;

    private Message message;
    @BeforeEach
    void setup() {
        homer = new User("homer@simpson.com", "spengergasse".toCharArray(), "Homer Simpson", "Rh.D.", "United Kingdom");
        bart = new User("bart@simpson.com", "spengergasse".toCharArray(), "Bart Simpson", "Ph.D.", "United States");
        message = new Message(homer, Instant.now(), new TextContent("some text"), List.of(new Media("path", "mime type", 123)),  Status.SENT); //TODO MAYBE CHANGE MEDIA FROM NULL TO SOME ACTUAL VALUE
    }

    @AfterEach
    void deleteFromRepo() {
        ChatRepository.findAll().forEach(chat1 -> ChatRepository.deleteById(chat1.getId()));
        if (homer != null)
            UserRepository.deleteById(homer.getId());
        if(bart != null)
            UserRepository.deleteById(bart.getId());
    }


    @Test
    void setSender_shouldSetSender_WhenPassingUser(){
        try{
            assertEquals(message.getSender(), homer);
            message.setSender(bart);
            assertEquals(message.getSender(), bart);
        }catch (Exception e){
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void setSender_shouldThrow_WhenSenderIsNull(){
        try {
            assertThrowsExactly(AssertException.class, () -> message.setSender(null));
        }catch (Exception e){
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void setContent_shouldSetContent_WhenPassingContent(){
        try{
            assertEquals(message.getContent().toString(), new TextContent("some text").toString());
            message.setContent(new TextContent("some test text"));
            assertEquals(message.getContent().toString(), new TextContent("some test text").toString());
        }catch (Exception e){
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void setContent_shouldThrow_WhenContentIsNull(){
        try {
            assertThrowsExactly(AssertException.class, () -> message.setContent(null));
        }catch (Exception e){
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void setTimestamp_shouldThrow_WhenPassingNull(){
        try{
            assertThrowsExactly(AssertException.class, () -> message.setTimestamp(null));
        }catch (Exception e){
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    //TODO
    @Test
    void setAttachments_shouldSetAttachments_WhenPassingListOfMedia(){

    }

}