package domain.User;

import domain.common.Image;
import static foundation.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Profile {

    // not null, min 1 character, max 128 characters
    private String name;
    // not null, min 1 character, max 128 characters
    private String title;
    // not null, not blank
    private Country location;
    // not null, TODO max
    private Set<Specialization> tags;
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

    public Profile(String name, String title, Country location, Set<Specialization> tags, Integer rating, Image avatar) {
        this.name = name; // TODO
        this.title = title; // TODO
        this.location = location; // TODO
        this.tags = tags; // TODO
        this.rating = rating; // TODO
        this.avatar = avatar; // TODO
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

    //TODO finish and test and implement add method
    public void setTags(String ... tags) {
        isNotNull(tags, "tags");

        Set<String> safeTags = Arrays.stream(tags).filter(tag -> Specialization.getSpecializations().contains(tag)).collect(Collectors.toSet());
        safeTags.forEach(tag -> this.tags.add(new Specialization(tag)));

        //TODO Max assertion
        //TODO Talk about a solution of this method. Should setTags only do this.tags = new Hashset<>() or also add Objects to the set? How do we want to solve this Problem
    }


    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }
}
