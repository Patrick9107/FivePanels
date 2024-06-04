package domain.Medicalcase;

import domain.User.UserException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CaseTag {

    // not null
    private static final Set<String> caseTags;

    private String tag;

    public CaseTag(String tag) {
        setTag(tag);
    }

    public void setTag(String tag) {
        if (!(caseTags.contains(tag)))
            throw new MedicalcaseException("setTag(): tag does not exist");
        this.tag = tag;
    }

    public static Set<String> getCaseTags() {

        return caseTags;
    }

    static {
        String path = "src/main/resources/casetags.txt";
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            caseTags = stream.collect(Collectors.toSet());

        } catch (IOException e) {
            throw new MedicalcaseException(e.getMessage() + STR."error reading file \{path}");
        }
    }

    @Override
    public String toString() {
        return tag;
    }
}
