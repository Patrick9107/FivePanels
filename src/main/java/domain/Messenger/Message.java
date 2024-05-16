package domain.Messenger;

import domain.User.User;
import domain.common.Media;
import domain.common.TextContent;
import foundation.Assert;

import java.time.Instant;
import java.util.List;

public class Message {

    // not null
    private User sender;
    // not null
    private Instant timestamp;
    // not null
    private TextContent content;
    // max 8 entries
    private List<Media> attachments;
    // not null
    private Status status;

    public Message(User sender, Instant timestamp, TextContent content, List<Media> attachments, Status status) {
        setSender(sender);
        setTimestamp(timestamp);
        setContent(content);
        setAttachments(attachments);
        setStatus(status);
    }

    public void setSender(User sender) {
        Assert.isNotNull(sender, "sender");
        this.sender = sender;
    }

    public void setTimestamp(Instant timestamp) {
        Assert.isNotNull(timestamp, "timestamp");
        this.timestamp = timestamp;
    }

    public void setContent(TextContent content) {
        Assert.isNotNull(content, "content");
        this.content = content;

    }

    public void setAttachments(List<Media> attachments) {
        Assert.isNotNull(attachments, "attachments");
        this.attachments = attachments;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
