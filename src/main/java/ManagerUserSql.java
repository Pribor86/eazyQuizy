import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerUserSql implements Manager {

    private Connection connection;
    private List<ClassUser> classUsers = new ArrayList<>();

    public ManagerUserSql(Connection connection) {
        this.connection = connection;
    }

    public boolean createUser(ClassUser classUser) {
        boolean rsl = false;
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO player (name, correct, wrong) values (?, 0, 0)"
        )) {
            ps.setString(1, classUser.getName());
            ps.execute();
            rsl = true;
        } catch (SQLException sql) {
            System.out.println("Hey " + classUser.getName() + ", I remember you played before."
            + System.lineSeparator() + "Don't you remember me?"
            + System.lineSeparator() + "Let's continue instead."
            + System.lineSeparator());
        }
        return rsl;
    }

    public ClassUser getUserByName(String name) {
        ClassUser classUser = new ClassUser();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM player where name = ?"
        )) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    classUser.setName(rs.getString("name"));
                    classUser.setRightAnswers(rs.getInt("correct"));
                    classUser.setWrongAnswers(rs.getInt("wrong"));
                    return classUser;
                }
            }
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(ClassUser classUser) {
        boolean rsl = false;
        try (PreparedStatement ps =
                     connection.prepareStatement(
                             "update player set correct = ?, wrong = ? where name = ?")) {

            ps.setInt(1, classUser.getRightAnswers());
            ps.setInt(2, classUser.getWrongAnswers());
            ps.setString(3, classUser.getName());
            rsl = ps.executeUpdate() > 0;
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
        return rsl;
    }

    public List<ClassUser> getUserList() {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM player")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    boolean check = true;
                    for (ClassUser user : classUsers) {
                        if (user.getName().equals(name)) {
                            check = false;
                            break;
                        }
                    }
                    if (check) {
                        ClassUser classUser = new ClassUser();
                        classUser.setName(name);
                        classUser.setRightAnswers(rs.getInt("correct"));
                        classUser.setWrongAnswers(rs.getInt("wrong"));
                        classUsers.add(classUser);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("...No existing users in DB");
        }
        return classUsers;
    }

    private void createTable() {
        try (PreparedStatement ps = connection.prepareStatement(
                "CREATE TABLE player (name varchar(30) NOT NULL UNIQUE, correct int, wrong int)")) {
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
