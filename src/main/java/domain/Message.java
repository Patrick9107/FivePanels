package domain;

import java.time.Instant;
import java.util.List;

public class Message {

    private User sender;
    private Instant timestamp;
    private TextContent content;
    private List<Media> attachments;
    private Status status;
}
