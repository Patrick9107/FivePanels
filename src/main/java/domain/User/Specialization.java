package domain.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Specialization {

    // not null
    private static Set<String> specializations;

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

    public static void main(String[] args) {
        System.out.println(specializations);
    }
}
