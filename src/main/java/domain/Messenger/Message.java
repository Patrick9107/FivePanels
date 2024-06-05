package domain.Messenger;

import domain.User.User;
import domain.common.Media;
import domain.common.TextContent;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import static foundation.Assert.*;

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
        isNotNull(sender, "sender");
        this.sender = sender;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public void setContent(TextContent content) {
        isNotNull(content, "content");
        this.content = content;

    }

    public void setAttachments(List<Media> attachments) {
        this.attachments = attachments;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(sender, message.sender) && Objects.equals(timestamp, message.timestamp) && Objects.equals(content, message.content) && Objects.equals(attachments, message.attachments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, timestamp, content, attachments);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(sender.getProfile().getName()).append(" ").append(timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime().toString()).append("\n");
        sb.append(content).append("\n");
        if (attachments != null) {
            attachments.forEach(media -> sb.append(media).append(", "));
            for (int i = 0; i < 2; i++) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("\n");
        }
//        sb.append(status.toString()).append("\n");
        return sb.toString();
    }
}
