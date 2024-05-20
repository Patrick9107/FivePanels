package domain.Medicalcase;

import java.util.UUID;

public class Vote {

    // not null, max entries = number of voting options on that medicalcase
    private int percentage;

    private Answer answer;

    public Vote(int percentage, Answer answer) {
        setPercentage(percentage);
        setAnswer(answer);
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public int getPercentage() {
        return percentage;
    }

    public Answer getAnswer() {
        return answer;
    }
}
