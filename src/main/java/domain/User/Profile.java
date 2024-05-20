package domain.User;

import domain.common.Image;
import static foundation.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Profile {

    // not null, min 1 character, max 128 characters
    private String name;
    // not null, min 1 character, max 128 characters
    private String title;
    // not null, not blank
    private Country location;
    // not null
    private Set<Specialization> tags;
    // not null
    private Integer rating;
    private Image avatar;

    // TODO NUR ZUM TESTEN KANN WIEDER GELÖSCHT WERDEN
    public Profile() {
        name = "admin";
        title = null;
        location = null;
        tags = null;
        rating = null;
        avatar = null;
    }

    public Profile(String name, String title, String location, String... tags) {
        setName(name);
        setTitle(title);
        setLocation(location);
        setTags(tags);
        this.rating = 0;
    }

    public void setName(String name) {
        isNotNull(name, "name");
        isGreaterThanOrEqual(name.length(), "name", 1, "1");
        hasMaxLength(name, 129, "name");
        this.name = name;
    }

    public void setTitle(String title) {
        isNotNull(title, "title");
        isGreaterThanOrEqual(title.length(), "title", 1, "1");
        hasMaxLength(title, 129, "title");
        this.title = title;
    }

    public void setLocation(String location) {
        isNotNull(location, "location");
        isNotBlank(location, "location");

        this.location = new Country(location);
    }

    public void setTags(String... tags) {
        this.tags = new HashSet<>();
        Arrays.stream(tags).forEach(this::addTag);
    }

    public void addTag(String tag) {
        isNotNull(tag, "tag");
        tags.add(new Specialization(tag));
    }

    public String getName() {
        return name;
    }

    public void addRating(int rating) {
        this.rating += rating;
    }
    // TODO
    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    // TODO rewrite
    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", location=" + location +
                ", tags=" + tags +
                ", rating=" + rating +
                ", avatar=" + avatar +
                '}';
    }
}
