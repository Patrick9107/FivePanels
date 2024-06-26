package domain.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static foundation.Assert.*;

public class Specialization {

    // not null
    private static final Set<String> specializations;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Specialization that = (Specialization) o;
        return Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }

    @Override
    public String toString() {
        return tag;
    }
}
