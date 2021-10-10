import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MathQuGen {

    private Connection connection;

    public MathQuGen(Connection connection) {
        this.connection = connection;
    }

    public ClassQuestion getAnswer(int question) {
        Random ran = new Random();
        List<String> rAns = new ArrayList<>();
        List<String> wAns = new ArrayList<>();
        ClassQuestion cq = new ClassQuestion(String.valueOf(question));

        int otnimaemoe = ran.nextInt(1, question - 3);
        int toOtChegoOtnimajut = question - otnimaemoe;
        rAns.add(question + " = " + toOtChegoOtnimajut + " + " + otnimaemoe);
        wAns.add(question + " = " + (toOtChegoOtnimajut - 1) + " + " + otnimaemoe);

        int otnimaemoe2;
        do {
            otnimaemoe2 = ran.nextInt(1, question - 3);
        } while (otnimaemoe2 == otnimaemoe);

        int toOtChegoOtnimajut2 = question - otnimaemoe2;
        rAns.add(question + " = " + toOtChegoOtnimajut2 + " + " + otnimaemoe2);
        wAns.add(question + " = " + (toOtChegoOtnimajut2 - 2) + " + " + otnimaemoe2);

        int otnimaemoe3;
        do {
            otnimaemoe3 = ran.nextInt(1, question - 3);
        } while (otnimaemoe3 == otnimaemoe2 || otnimaemoe3 == otnimaemoe);

        int toOtChegoOtnimajut3 = question - otnimaemoe3;
        rAns.add(question + " = " + toOtChegoOtnimajut3 + " + " + otnimaemoe3);
        wAns.add(question + " = " + (toOtChegoOtnimajut3 - 3) + " + " + otnimaemoe3);

        cq.setRightAnswer(rAns);
        cq.setWrongAnswer(wAns);

        uploadToDB(cq);

        return cq;
    }

    private void uploadToDB(ClassQuestion cq) {
        int genKey = -1;
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO question (question, difficulty, topic_id) "
                       + "values (?, 1, 1)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cq.getQuestion());
            ps.execute();
            try (ResultSet genKeys = ps.getGeneratedKeys()) {
                if (genKeys.next()) {
                    genKey = genKeys.getInt(1);
                }
            }

            try (PreparedStatement psR = connection.prepareStatement(
                    "INSERT INTO answer (answer, iscorrect, question_id) "
                            + "values (?, ?, ?)")) {

                for (String gg : cq.getRightAnswer()) {
                    psR.setString(1, gg);
                    psR.setBoolean(2, true);
                    psR.setInt(3, genKey);
                    psR.addBatch();
                }

                for (String gd : cq.getWrongAnswer()) {
                    psR.setString(1, gd);
                    psR.setBoolean(2, false);
                    psR.setInt(3, genKey);
                    psR.addBatch();
                }

                psR.executeBatch();
            }
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
    }

}
