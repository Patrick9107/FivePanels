package domain.Medicalcase;

import domain.Messenger.Chat;
import domain.common.BaseEntity;
import domain.common.Content;
import domain.User.User;
import repository.MedicalcaseRepository;
import repository.UserRepository;

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

    /**
     * Constructs a new {@link Medicalcase} instance with the specified title, owner, name, and tags.
     * Also initializes the medicalcase's content, member, reaction, votingOptions, votes, chat and correctAnswer.
     *
     * @param title    the medicalcase's titles
     * @param owner    the medicalcase's owner
     * @param tags     the medicalcase's tags
     */
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
        MedicalcaseRepository.save(this);
    }

    public void setTitle(String title) {
        if (published)
            throw new MedicalcaseException(STR."setTitle(): can not change title for a published medicalcase");
        hasMaxLength(title, 129, "title");
        this.title = title;
        if (chat != null)
            chat.setName(title);
    }

    public String getTitle() {
        return title;
    }

    private void setOwner(User owner) {
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
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        if (!published)
            throw new MedicalcaseException(STR."setCorrectAnswer(): can not set correctAnswer in non public medicalcase");
        isNotNull(correctAnswer, "correctAnswer");
        if (!(votingOptions.contains(correctAnswer))) {
            throw new MedicalcaseException(STR."setCorrectAnswer(): correctAnswer has to be in the votingOption!");
        }
        this.correctAnswer = correctAnswer;
        evaluateVotes();
    }

    public Chat getChat() {
        return chat;
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
        isNotNull(user, "user");
        hasMaxSize(reactions, 513, "reactions");
        reactions.add(user.getId());
    }

    public void addMember(User user) {
        if (!published)
            throw new MedicalcaseException(STR."addMember(): can only add members to a published medicalcase");
        hasMaxSize(members, 513, "members");
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
        try {
            content.remove(index);
        } catch (IndexOutOfBoundsException e) {
            throw new MedicalcaseException(STR."removeContent(): Index out of bounds (\{index})");
        }
    }

    public void removeContent(Content contentToRemove) {
        if (published)
            throw new MedicalcaseException(STR."removeContent(): can not remove content from a published medicalcase");
        isNotNull(contentToRemove, "contentId");

        content.stream().filter(content1 -> content1.equals(contentToRemove)).findFirst().ifPresent(content1 -> content.remove(content1));
    }

    public void viewAvgVotesPerAnswer() {
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

    public void viewTotalPointsPerAnswer() {
        List<Vote> allVotes = votes.values().stream().flatMap(Set::stream).toList();
        Map<String, Double> voteCounts = allVotes.stream().collect(Collectors.groupingBy(vote ->
                vote.getAnswer().getAnswer(), Collectors.summingDouble(Vote::getPercentage)));
        voteCounts.forEach((option, count) -> System.out.println(STR."\{option}: \{count}"));
    }

    public Set<User> getMembers() {
        return members;
    }

    public void castVote(User user, String answer, int percentage) {
        isNotNull(user, "user");
        isNotBlank(answer, "answer");
        isGreaterThanOrEqual(percentage, "percentage", 0, "zero");

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
    }

    public void evaluateVotes() {
        //gibt die User zurück die für die richtige antwort gevotet haben.
        Map<UUID, Vote> userAndCorrectAnswer = votes.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .filter(vote -> correctAnswer.getAnswer().equals(vote.getAnswer().getAnswer()))
                        .map(vote -> Map.entry(entry.getKey(), vote)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        //addiert die Prozentzahl die der user für die richtige antwort angegeben hat zu seinem rating.
        // was ich gemacht habe:
        //userAndCorrectAnswer.entrySet().forEach(value -> UserRepository.findById(value.getKey()).ifPresent(user -> user.getProfile().setRating(value.getValue().getPercentage())));
        //Verbessert durch IDE
        userAndCorrectAnswer.forEach((key, value1) -> UserRepository.findById(key).ifPresent(user -> user.getProfile().addRating(value1.getPercentage())));
    }

    public void viewChat(User user) {
        chat.viewChat(user);
    }
}
