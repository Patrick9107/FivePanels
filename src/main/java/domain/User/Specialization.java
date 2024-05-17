package domain.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static foundation.Assert.*;

public class Specialization {

    // not null
    private static Set<String> specializations;

    // not null, has to be in specializations set
    private String tag;

    public Specialization(String tag) {
        setTag(tag);
    }

    public void setTag(String tag) {
        if (!(specializations.contains(tag)))
            throw new UserException("setTag(): tag does not exist");
        this.tag = tag;
    }

    public static Set<String> getSpecializations() {

        return specializations;
    }

    static {
        String path = "src/main/resources/specializations.txt";
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            specializations = stream.collect(Collectors.toSet());

        } catch (IOException e) {
            throw new UserException(e.getMessage() + STR."error reading file \{path}");
        }
    }
}
