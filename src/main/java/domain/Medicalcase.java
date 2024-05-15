package domain;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Medicalcase extends BaseEntity {

    private String title;
    private User owner;
    private List<Content> content;
    private Set<User> members;
    private Set<User> reactions;
    private Set<CaseTag> tags;
    private LinkedHashSet<String> votingOptions;
    private Set<Vote> votes;

    public void react(User user){

    }

    public void addMember(User user){

    }

    public void edit(){

    }

    public void viewVotes(){

    }

    public void viewMembers(){

    }

    public void castVote(User user){

    }

    public void evaluateVotes(){

    }

    public void openChat(){

    }
}
