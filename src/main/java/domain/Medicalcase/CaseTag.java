package domain.Medicalcase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CaseTag {

    // not null
    private static Set<String> caseTags;

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
}
