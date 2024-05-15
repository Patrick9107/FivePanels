package domain;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class CaseTag {

    public static Set<String> caseTag;

    public static Set<String> caseTags() {

        return caseTag;
    }

    public static void readTagsFromFile(String filename) {

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String line;
            while ((line = br.readLine()) != null) {

                caseTag.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new MedicalcaseException(STR."\{e.getMessage()}: file was not found");
        } catch (IOException e) {
            throw new MedicalcaseException(STR."\{e.getMessage()}: error reading file");
        }
    }

}
