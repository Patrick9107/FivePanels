package domain;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public abstract class Hashtag {

    public static Set<String> tags;

    public static Set<String> getTags() {

        return tags;
    }

    public static void readTagsFromFile(String filename) {

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String line;
            while ((line = br.readLine()) != null) {

                tags.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new MedicalcaseException(STR."\{e.getMessage()}: file was not found");
        } catch (IOException e) {
            throw new MedicalcaseException(STR."\{e.getMessage()}: error reading file");
        }
    }


    public static void main(String[] args) {
//        CaseTag caseTag = new CaseTag();
//        Specialization specialization = new Specialization();
        System.out.println(CaseTag.tags);
        System.out.println(Specialization.tags);
    }
}
