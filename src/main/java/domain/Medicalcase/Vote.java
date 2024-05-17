package domain.Medicalcase;

import java.util.Map;
import java.util.UUID;

public class Vote {

    // not null
    private UUID user;
    // not null, max entries = number of voting options on that medicalcase
//    private Map<String, Integer> answers;

    private int percentage;
}
