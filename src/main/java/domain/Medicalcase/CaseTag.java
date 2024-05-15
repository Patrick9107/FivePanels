package domain.Medicalcase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public abstract class CaseTag {

    // not null
    private static Set<String> caseTags;

    public static Set<String> getCaseTags() {

        return caseTags;
    }

    public static void readTagsFromFile(String filename) {

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String line;
            while ((line = br.readLine()) != null) {

                caseTags.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new MedicalcaseException(STR."\{e.getMessage()}: file was not found");
        } catch (IOException e) {
            throw new MedicalcaseException(STR."\{e.getMessage()}: error reading file");
        }
    }

    static {
        caseTags = new HashSet<>();
        readTagsFromFile("src/main/resources/casetags.txt");
    }
}
