package domain;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Chat extends BaseEntity {

    private String name;
    private Set<UUID> members;
    private List<Message> history;
}
