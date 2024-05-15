package domain;

import java.util.HashSet;

public abstract class Specialization extends Hashtag {

    static {
        tags = new HashSet<>();
        readTagsFromFile("src/main/resources/specializations.txt");
    }
}
