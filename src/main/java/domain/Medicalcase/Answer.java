package domain.Medicalcase;

import static foundation.Assert.*;

public class Answer {

    // not null, not blank, max 255 characters
    private String answer;

    public Answer(String answer) {
        setAnswer(answer);
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        hasMaxLength(answer, 256, "answer");
        this.answer = answer;
    }
}
