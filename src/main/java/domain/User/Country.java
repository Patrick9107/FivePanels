package domain.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Country {
    // not null
    private static Set<String> countries;

    public static Set<String> getCountries() {

        return countries;
    }

    static {
        String path = "src/main/resources/countries.txt";
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            countries = stream.collect(Collectors.toSet());

        } catch (IOException e) {
            throw new UserException(e.getMessage() + STR."error reading file \{path}");
        }
    }
}
