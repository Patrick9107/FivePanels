package domain.Medicalcase;

import domain.common.BaseEntity;
import domain.common.Content;
import domain.User.User;

import static foundation.Assert.*;

import java.util.*;

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
    private Set<UUID> reactions;
    // not null
    private Set<CaseTag> tags;
    // not null, max 8 entries
    private Set<String> votingOptions;
    // not null, max entries = number of members * votingOptions -> total max 4096
    private Set<Vote> votes;

    public Medicalcase(String title, User owner, String... tags) {
        setTitle(title);
        setOwner(owner);
        this.content = new LinkedList<>();
        this.members = new HashSet<>();
        this.reactions = new HashSet<>();
        setTags(tags);
        this.votingOptions = new LinkedHashSet<>();
        this.votes = new HashSet<>();
    }

    public void setTitle(String title){
        hasMaxLength(title, 129, "title");
        this.title = title;
    }

    public void setOwner(User owner) {
        if (members.contains(owner))
            throw new MedicalcaseException(STR."setOwner(): member of medicalcase can not be the owner");
        this.owner = owner;
    }

    public void setTags(String... tags) {
        this.tags = new HashSet<>();
        Arrays.stream(tags).forEach(this::addTag);
    }

    public void addTag(String tag) {
        isNotNull(tag, "tag");
        tags.add(new CaseTag(tag));
    }

    public void react(User user) {
        isNotNull(user , "user");
        hasMaxSize(reactions, 513, "reactions");

        reactions.add(user.getId());
    }

    public void addMember(User user) {
        hasMaxSize(members, 513 , "members");
        isNotNull(user, "user");

        members.add(user);
    }

    // TODO "Der Owner kann Antworten beim Erstellen vorgeben"
    // heißt das er kann keine antworten mehr hinzufügen/entfernen nachdem den case erstellt hat? oder machen wir ein zustätzliches attribut, welches sagt ob der case pubic ist oder nicht. Und solange er nicht public ist kann er noch antwortmöglichkeiten hinzufügen/entfernen.
    public void addVotingOption(String option) {
        isNotNull(option, "votingOption");
        hasMaxSize(votingOptions, 9, "votingOptions");

        votingOptions.add(option);
    }

    public void removeVotingOption(String option) {
        isNotNull(option, "votingOption");

        if (!(votingOptions.contains(option)))
            throw new MedicalcaseException(STR."removeVotingOption(): no such option");
        votingOptions.remove(option);
    }

    public void addContent(Content content) {
        isNotNull(content, "content");

        this.content.add(content);
    }

    public void removeContent(int index) {
        if (content.size() <= index)
            throw new MedicalcaseException(STR."removeContent(): Index out of bound");
        content.remove(index);
    }

    public void removeContent(Content contentToRemove) {
        isNotNull(contentToRemove, "contentId");

        content.stream().filter(content1 -> content1.equals(contentToRemove)).findFirst().ifPresent((content1 -> content.remove(content1));
    }

    public void edit(){

    }

    // TODO Der Owner kann immer das aktuelle Voting-Resultat anzeigen lassen
    public void viewVotes(){

    }

    public void viewMembers(){

    }

    // TODO Die Members können mittels prozentual Verteilung auf die Antworten voten.
    public void castVote(User user){

    }

    // TODO Alle Voter, die auf die korrekte Antwort den höchsten prozentualen Wert gegeben haben, bekommen +X Score Punkte
    public void evaluateVotes(){

    }

    public void openChat(){

    }
}
