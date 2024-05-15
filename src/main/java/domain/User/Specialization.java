package domain.User;

import domain.Medicalcase.MedicalcaseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public abstract class Specialization {

    // not null
    private static Set<String> specializations;

    public static Set<String> getSpecializations() {

        return specializations;
    }

    public static void readSpecializationsFromFile(String filename) {

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String line;
            while ((line = br.readLine()) != null) {

                specializations.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new MedicalcaseException(STR."\{e.getMessage()}: file was not found");
        } catch (IOException e) {
            throw new MedicalcaseException(STR."\{e.getMessage()}: error reading file");
        }
    }
    static {
        specializations = new HashSet<>();
        readSpecializationsFromFile("src/main/resources/specializations.txt");
    }
}
