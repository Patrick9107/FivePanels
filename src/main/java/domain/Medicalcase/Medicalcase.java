package domain.Medicalcase;

import domain.Messenger.Chat;
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
    private Set<Answer> votingOptions;
    //can be null -> not null only when evaluating Votes
    private Answer correctAnswer;
    // not null, max entries = number of members * votingOptions -> total max 4096
    private Map<UUID, Set<Vote>> votes;
    // true if medicalcase is published, false otherwise
    private boolean published;
    // not null, all members of medicalcase are automatically in this chat
    private Chat chat;

    // TODO irgendwie speichern welcher chat zu welchem medicalcase gehört, probably im repository

    public Medicalcase(String title, User owner, String... tags) {
        setTitle(title);
        setOwner(owner);
        this.content = new LinkedList<>();
        this.members = new HashSet<>();
        this.reactions = new HashSet<>();
        setTags(tags);
        this.votingOptions = new LinkedHashSet<>();
        this.votes = new HashMap<>();
        this.chat = new Chat(title, Set.of(owner.getId()), true);
        this.correctAnswer = null;
    }

    public void setTitle(String title) {
        if (published)
            throw new MedicalcaseException(STR."setTitle(): can not change title for a published medicalcase");
        hasMaxLength(title, 129, "title");
        this.title = title;
        chat.setName(title);
    }

    public void setOwner(User owner) {
        isNotNull(owner, "owner");
        this.owner = owner;
    }

    public void setTags(String... tags) {
        if (published)
            throw new MedicalcaseException(STR."setTags(): can not set tags for a published medicalcase");
        this.tags = new HashSet<>();
        Arrays.stream(tags).forEach(this::addTag);
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        isNotNull(correctAnswer,"correctAnswer");
        if(!votingOptions.contains(correctAnswer)) {
            this.correctAnswer = correctAnswer;
            throw new MedicalcaseException("setCorrectAnswer: correctAnswer has to be in the votingOption!");
        }
    }

    public void addTag(String tag) {
        if (published)
            throw new MedicalcaseException(STR."addTag(): can not add tag to a published medicalcase");
        isNotNull(tag, "tag");
        tags.add(new CaseTag(tag));
    }

    public void react(User user) {
        if (!published)
            throw new MedicalcaseException(STR."react(): can only react to a published medicalcase");
        isNotNull(user , "user");
        hasMaxSize(reactions, 513, "reactions");
        reactions.add(user.getId());
    }

    public void addMember(User user) {
        if (!published)
            throw new MedicalcaseException(STR."addMember(): can only add members to a published medicalcase");
        hasMaxSize(members, 513 , "members");
        isNotNull(user, "user");

        members.add(user);
        chat.addMember(user);
    }

    public void publish() {
        this.published = true;
    }

    public void addVotingOption(String option) {
        if (published)
            throw new MedicalcaseException(STR."addVotingOption(): can not add votingOption to a published medicalcase");
        isNotNull(option, "votingOption");
        hasMaxSize(votingOptions, 9, "votingOptions");

        votingOptions.add(new Answer(option));
    }

    public void removeVotingOption(Answer option) {
        if (published)
            throw new MedicalcaseException(STR."removeVotingOption(): can not remove votingOption from a published medicalcase");
        isNotNull(option, "votingOption");
        if (!(votingOptions.contains(option)))
            throw new MedicalcaseException(STR."removeVotingOption(): no such option");
        votingOptions.remove(option);
    }

    public void addContent(Content content) {
        if (published)
            throw new MedicalcaseException(STR."addContent(): can not add content to a published medicalcase");
        isNotNull(content, "content");

        this.content.add(content);
    }

    public void addContent(Content content, int index) {
        if (published)
            throw new MedicalcaseException(STR."addContent(): can not add content to a published medicalcase");
        isNotNull(content, "content");
        try {
            this.content.add(index, content);
        } catch (IndexOutOfBoundsException e) {
            throw new MedicalcaseException(STR."addContent(): Index out of bounds (\{index})");
        }
    }

    public void removeContent(int index) {
        if (published)
            throw new MedicalcaseException(STR."removeContent(): can not remove content from a published medicalcase");
        if (content.size() <= index || index < 0)
            throw new MedicalcaseException(STR."removeContent(): Index out of bound");
        content.remove(index);
    }

    public void removeContent(Content contentToRemove) {
        if (published)
            throw new MedicalcaseException(STR."removeContent(): can not remove content from a published medicalcase");
        isNotNull(contentToRemove, "contentId");

        content.stream().filter(content1 -> content1.equals(contentToRemove)).findFirst().ifPresent(content1 -> content.remove(content1));
    }

    // TODO Der Owner kann immer das aktuelle Voting-Resultat anzeigen lassen
    public void viewVotes(){
        // mit votes.values() bekommt man eine collection mit allen listen von votes
//        votes.values().forEach(votes1 -> votes1.stream().forEach(vote -> vote.getPercentage()));
    }

    public void viewMembers(){

    }
    // TODO Die Members können mittels prozentual Verteilung auf die Antworten voten.
    // ka ob das so passt aber das ist mein erster ansatz LG
    public void castVote(User user, String answer, int percentage) {
        isNotNull(user, "user");
        isNotBlank(answer, "answer");
        isGreaterThanOrEqual(percentage, "percentage", 0, "zero");

        if (votes.get(user.getId()).stream().mapToInt(Vote::getPercentage).sum() + percentage > 100)
            throw new MedicalcaseException(STR."castVote(): can not vote with more than 100 percent");

        Answer newAnswer = new Answer(answer);
        if (!(votingOptions.contains(newAnswer)))
            throw new MedicalcaseException(STR."castVote(): answer does not exist");

        if (!(votes.containsKey(user.getId())))
            votes.put(user.getId(), new HashSet<>());

        if (votes.get(user.getId()).stream().noneMatch(vote -> vote.getAnswer().getAnswer().equals(answer)))
            throw new MedicalcaseException(STR."castVote(): can not vote same answer twice");
        votes.get(user.getId()).add(new Vote(percentage, newAnswer));
    }

    // TODO Alle Voter, die auf die korrekte Antwort den höchsten prozentualen Wert gegeben haben, bekommen +X Score Punkte
    public void evaluateVotes(){

    }

    public void viewChat(){

    }
}
