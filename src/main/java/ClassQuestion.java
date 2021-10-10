import java.util.ArrayList;
import java.util.List;

public class ClassQuestion {
    private final String question;
    private List<String> rightAnswer = new ArrayList<>();
    private List<String> wrongAnswer = new ArrayList<>();

    public ClassQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(List<String> rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public List<String> getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(List<String> wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    @Override
    public String toString() {
        String q = "Question: " + question;
        StringBuilder ra = new StringBuilder();
        StringBuilder wa = new StringBuilder();

        for (String ss : rightAnswer) {
            ra.append(ss).append(System.lineSeparator());
        }

        for (String ss : wrongAnswer) {
            wa.append(ss).append(System.lineSeparator());
        }
        return q + System.lineSeparator()
                + "Right answers: " + ra + System.lineSeparator()
                + "Wrong answers: " + wa + System.lineSeparator();
    }
}
