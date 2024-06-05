package domain.Medicalcase;

import java.util.Objects;

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
        this.answer = hasMaxLength(answer, 256, "answer");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer1 = (Answer) o;
        return Objects.equals(answer, answer1.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answer);
    }
}
