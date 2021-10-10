public class InputDumb implements Input {
    private String[] answers;
    private String answer;
    private int position = 0;

    public InputDumb(String[] answers) {
        this.answers = answers;
    }

    @Override
    public String askStr(String question) {
        return answers[position++];
    }

    @Override
    public int askInt(String question) {
        return Integer.parseInt(askStr(question));
    }
}
