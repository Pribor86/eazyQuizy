import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GmenuGame implements Gmenu {

    private final Connection connection;
    private Output out;
    private Input in;
    private List<ClassQuestion> questionList = new ArrayList<>();
    private String topicName = "";
    private int difficulty = -1;
    private List<String> topicList = new ArrayList<>();
    private int rAns = 0;
    private int wAns = 0;
    private int questionIndex = 0;
    private boolean isTopicList = false;
    private final int questionsPerGame = 5;

    private final List<String> difficultyList = List.of("Easy", "Medium(soon...)", "Hard(soon...)");

    public GmenuGame(Connection connection, Input in, Output out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    @Override
    public void printGame(ClassUser user) {
        getTopicList();

        int topicNum = -1;
        while (topicNum != 0) {
            printTopicList();
            topicNum = in.askInt("Choose category: ");
            if (topicNum != 0) { //temporary loop as no other Topic but Mathematics
                out.println("This topic is under development, please choose another one.");
            }
        }
        topicName = topicList.get(topicNum);
        while (difficulty != 0) {
            printDifficultyList();
            difficulty = in.askInt("Choose difficulty: ");
            if (difficulty != 0) { //temporary loop as no other difficulty but easy one
                out.println("This difficulty is under development, please choose another one.");
            }
        }
        getQuestionList();
        for (int i = 0; i < questionsPerGame; i++) {
            questionManager();
        }

        out.println("Right answers: " + rAns);
        out.println("Wrong answers: " + wAns);
        uploadScore(user);
    }

    @Override
    public void getList() {
        for (ClassQuestion cq : questionList) {
            System.out.println(cq);
        }
    }

    private void checkAnswers(HashMap<String, Boolean> answerMap, List<String> answers,
                              int rightAns) {
        int r = 0;
        int w = 0;
        for (String g : answers) {
            if (answerMap.get(g)) {
                r++;
            } else {
                w++;
            }
        }

        if (rightAns - r != 0 || w > 0) {
            wAns++;
        } else {
            rAns++;
        }
    }

    private void questionManager() {
        threeSpaces();
        ClassQuestion cq = questionList.get(questionIndex);
        String question = cq.getQuestion();
        HashMap<String, Boolean> answerMap = new HashMap<>();
        List<String> chosenAnswers = new ArrayList<>();

        int rightAnsQ = new Random().nextInt(1, 4);
        int wrongAnsQ = 4 - rightAnsQ;
        for (int o = 0; o < rightAnsQ; o++) {
            answerMap.put(cq.getRightAnswer()
                    .get(o), true);
        }

        for (int o = 0; o < wrongAnsQ; o++) {
            answerMap.put(cq.getWrongAnswer()
                    .get(o), false);
        }

        List<String> answers = new ArrayList<>(answerMap.keySet());
        Collections.shuffle(answers);

        out.println(question + " = ?");

        int indx = 0;
        int qNum = -1;
        out.println("");
        while (true) {
            out.println("=================");
            for (String str : answers) {
                out.println(indx++ + ". " + str);
            }
            out.println("=================");
            out.println("9. Submit answers.");
            out.println(System.lineSeparator());
            indx = 0;
            do {
                qNum = in.askInt("Choose the answer: ");
                if (qNum == 9) {
                    break;
                }
            } while ((qNum < 0 || qNum > 3));
            if (qNum == 9) {
                break;
            }

            if (!answers.get(qNum).equals("chosen")) {
                chosenAnswers.add(answers.get(qNum));
                answers.set(qNum, "chosen");
            }
        }
        checkAnswers(answerMap, chosenAnswers, rightAnsQ);
        questionIndex++;
    }

    private void uploadScore(ClassUser user) {
        int currentCorr = 0;
        int currentWrong = 0;
        try (PreparedStatement download = connection.prepareStatement(
                "SELECT * FROM player where name = ?")) {
            download.setString(1, user.getName());
            try (ResultSet rl = download.executeQuery()) {
                if (rl.next()) {
                    currentCorr = rl.getInt("correct");
                    currentWrong = rl.getInt("wrong");
                }
            }

            try (PreparedStatement upload = connection.prepareStatement(
                    "UPDATE player SET correct = ?, wrong = ? WHERE name = ?")) {
                upload.setInt(1, currentCorr + rAns);
                upload.setInt(2, currentWrong + wAns);
                upload.setString(3, user.getName());
                upload.execute();
            }
            } catch (SQLException sql) {
                sql.printStackTrace();
        }
        rAns = 0;
        wAns = 0;
    }

    private void printDifficultyList() {
        int num = 0;
        for (String dif : difficultyList) {
            System.out.println(num++ + ". " + dif);
        }
        threeSpaces();
    }

    private void printTopicList() {
        int num = 0;
        for (String topic : topicList) {
            System.out.println(num++ + ". " + topic);
        }
        threeSpaces();
    }

    private void threeSpaces() {
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
    }

    private void getTopicList() {
        if (!isTopicList) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM topic")) {
                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        topicList.add(resultSet.getString("topic"));
                    }
                }
            } catch (SQLException sql) {
                sql.printStackTrace();
            }
            isTopicList = true;
        }
    }

    public void getQuestionList() {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT "
                        + "topic.id as topicID, question.id, question.difficulty, "
                        + "question.question, answer.answer, answer.iscorrect "
                        + "FROM question JOIN answer "
                        + "ON question.id=answer.question_id "
                        + "JOIN topic "
                        + "ON topic.id=question.topic_id "
                        + "where topic.topic = ? "
                        + "and question.difficulty = ? ")) {
            ps.setString(2, String.valueOf(difficulty + 1));
            ps.setString(1, topicName);

            String currentQ = "r67jrj";
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String qFromDB = rs.getString("question");
                    if (!currentQ.equals(qFromDB)) {
                        questionList.add(new ClassQuestion(qFromDB));
                        currentQ = qFromDB;
                    }
                    ClassQuestion q = questionList.get(questionList.size() - 1);
                    boolean isTrue = rs.getBoolean("iscorrect");
                    String answer = rs.getString("answer");
                    if (isTrue) {
                        q.getRightAnswer().add(answer);
                    } else {
                        q.getWrongAnswer().add(answer);
                    }
                }
            }
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}

