package domain;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Medicalcase extends BaseEntity {

    // not null, not blank, max 128
    private String title;
    // not null, not a member of the same Medicalcase
    private User owner;
    // not null
    private List<Content> content;
    // not null, max 512 members
    private Set<User> members;
    // not null, max 512 entries
    private Set<User> reactions;
    // not null, TODO max
    private Set<CaseTag> tags;
    // not null, max 8 entries
    private /*LinkedHash*/Set<String> votingOptions;
    // not null, max entries = number of members * votingOptions
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
