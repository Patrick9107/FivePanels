package domain.Medicalcase;

import domain.Messenger.Chat;
import domain.common.BaseEntity;
import domain.common.Content;
import domain.User.User;
import repository.MedicalcaseRepository;

import static foundation.Assert.*;

import java.util.*;
import java.util.stream.Collectors;

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
        save();
    }

    public void setTitle(String title) {
        if (published)
            throw new MedicalcaseException(STR."setTitle(): can not change title for a published medicalcase");
        hasMaxLength(title, 129, "title");
        this.title = title;
        if (chat != null)
            chat.setName(title);
        save();
    }

    public String getTitle() {
        return title;
    }

    public void setOwner(User owner) {
        isNotNull(owner, "owner");
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    public void setTags(String... tags) {
        isNotNull(tags, "tags");
        if (published)
            throw new MedicalcaseException(STR."setTags(): can not set tags for a published medicalcase");
        this.tags = new HashSet<>();
        Arrays.stream(tags).forEach(this::addTag);
        save();
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        if (!published)
            throw new MedicalcaseException(STR."setCorrectAnswer(): can not set correctAnswer in non public medicalcase");
        isNotNull(correctAnswer,"correctAnswer");
        if(!(votingOptions.contains(correctAnswer))) {
            throw new MedicalcaseException(STR."setCorrectAnswer(): correctAnswer has to be in the votingOption!");
        }
        this.correctAnswer = correctAnswer;
        save();
    }

    public Chat getChat() {
        return chat;
    }

    public void addTag(String tag) {
        if (published)
            throw new MedicalcaseException(STR."addTag(): can not add tag to a published medicalcase");
        isNotNull(tag, "tag");
        tags.add(new CaseTag(tag));
        save();
    }

    public void react(User user) {
        if (!published)
            throw new MedicalcaseException(STR."react(): can only react to a published medicalcase");
        isNotNull(user , "user");
        hasMaxSize(reactions, 513, "reactions");
        reactions.add(user.getId());
        save();
    }

    public void addMember(User user) {
        if (!published)
            throw new MedicalcaseException(STR."addMember(): can only add members to a published medicalcase");
        hasMaxSize(members, 513 , "members");
        isNotNull(user, "user");

        members.add(user);
        chat.addMember(user);
        save();
    }

    public void publish() {
        this.published = true;
        save();
    }

    public void addVotingOption(String option) {
        if (published)
            throw new MedicalcaseException(STR."addVotingOption(): can not add votingOption to a published medicalcase");
        isNotNull(option, "votingOption");
        hasMaxSize(votingOptions, 9, "votingOptions");

        votingOptions.add(new Answer(option));
        save();
    }

    public void removeVotingOption(Answer option) {
        if (published)
            throw new MedicalcaseException(STR."removeVotingOption(): can not remove votingOption from a published medicalcase");
        isNotNull(option, "votingOption");
        if (!(votingOptions.contains(option)))
            throw new MedicalcaseException(STR."removeVotingOption(): no such option");
        votingOptions.remove(option);
        save();
    }

    public void addContent(Content content) {
        if (published)
            throw new MedicalcaseException(STR."addContent(): can not add content to a published medicalcase");
        isNotNull(content, "content");

        this.content.add(content);
        save();
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
        save();
    }

    public void removeContent(int index) {
        if (published)
            throw new MedicalcaseException(STR."removeContent(): can not remove content from a published medicalcase");
        if (content.size() <= index || index < 0)
            throw new MedicalcaseException(STR."removeContent(): Index out of bound");
        content.remove(index);
        save();
    }

    public void removeContent(Content contentToRemove) {
        if (published)
            throw new MedicalcaseException(STR."removeContent(): can not remove content from a published medicalcase");
        isNotNull(contentToRemove, "contentId");

        content.stream().filter(content1 -> content1.equals(contentToRemove)).findFirst().ifPresent(content1 -> content.remove(content1));
        save();
    }

    public void viewVotes(){
        // mit votes.values() bekommt man eine collection mit allen listen von votes
        //gibt dir alle Votes zurück
        List<Vote> allVotes = votes.values().stream()
                                            .flatMap(Set::stream)
                                            .toList();


        double percentSum = allVotes.stream().mapToDouble(Vote::getPercentage).sum();

        //addiert alle percentages und grupperit sie nach antworten
        Map<String, Double> voteCounts = allVotes.stream()
                                                .collect(Collectors.groupingBy(vote -> vote.getAnswer().getAnswer(), Collectors.averagingDouble(Vote::getPercentage)));

//        Map<String, Double> votePercentagePerVote =  voteCounts.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() / members.size()));

        //gibt die antworten und die anzahl der votes für jede antwort aus
        voteCounts.forEach((option, count) -> System.out.println(option + ": " + count));
    }

    public Set<User> getMembers(){
        return members;
    }

    public void castVote(User user, String answer, int percentage) {
        isNotNull(user, "user");
        isNotBlank(answer, "answer");
        isGreaterThanOrEqual(percentage, "percentage", 0, "zero");

        if (user.equals(owner))
            throw new MedicalcaseException(STR."castVote(): owner can not vote");

        if (!(votes.containsKey(user.getId())))
            votes.put(user.getId(), new HashSet<>());

        if (votes.get(user.getId()).stream().mapToInt(Vote::getPercentage).sum() + percentage > 100)
            throw new MedicalcaseException(STR."castVote(): can not vote with more than 100 percent");

        Answer newAnswer = new Answer(answer);
        if (!(votingOptions.contains(newAnswer)))
            throw new MedicalcaseException(STR."castVote(): answer does not exist");

        // derzeit ist es so, dass wenn ein user bereits auf eine Antwortmöglichkeit gevotet hat,
        // dass er garnicht mehr diese antwort voten kann, sprich er kann sich auch nicht ändern oder so
        // aber man kann es ändern wenn man lustig ist
        if (votes.get(user.getId()).stream().anyMatch(vote -> vote.getAnswer().getAnswer().equals(answer)))
            throw new MedicalcaseException(STR."castVote(): can not vote same answer twice");
        votes.get(user.getId()).add(new Vote(percentage, newAnswer));
        save();
    }

    // TODO Alle Voter, die auf die korrekte Antwort den höchsten prozentualen Wert gegeben haben, bekommen +X Score Punkte
    public void evaluateVotes(){

    }

    public void viewChat(){
        System.out.println(STR."""
\{title}
\{chat.toString()}""");
    }

    @Override
    public void save() {
        MedicalcaseRepository.save(this);
    }
}
