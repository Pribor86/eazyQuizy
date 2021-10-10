import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class StartUI {
    private Connection connection;
    private Output out;
    private Properties cfg = new Properties();

    public StartUI(Output out) {
        this.out = out;
    }

    public void initConnection() {
            try (InputStream in = StartUI.class.getClassLoader()
                    .getResourceAsStream("app.properties")) {
                cfg.load(in);

                Class.forName(cfg.getProperty("driver-class-name"));

                connection = DriverManager.getConnection(
                        cfg.getProperty("urlEQ"),
                        cfg.getProperty("username"),
                        cfg.getProperty("password")
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void showMainMenu(List<Mmenu> menuList) {
        out.println("Menu");
        int index = 0;
        for (Mmenu menu : menuList) {
            out.println(index + ". " + menu.name());
            index++;
        }
    }

    public void run(List<Mmenu> menu, Input in, Manager manager) {
        boolean run = true;
        while (run) {
            showMainMenu(menu);
            int select = in.askInt("Select: ");
            if (select < 0 || select >= menu.size()) {
                out.println(System.lineSeparator()
                + "Buddy, you hit the wrong number! Please try again."
                + System.lineSeparator());
                continue;
            }
            Mmenu menuAction = menu.get(select);
            run = menuAction.execute(in, manager);
        }
    }

    private void questionGenToDB() {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM question where id = 50")) {

        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                MathQuGen mGen = new MathQuGen(connection);
                for (int i = 0; i < 100; i++) {
                    mGen.getAnswer(new Random().nextInt(20, 250));
                }
            }
        }
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Output out = new OutputConsole();
        Input in = new InputValidate(out, new InputConsole());
        StartUI start = new StartUI(out);
        DbConnect mdb = new DbConnect();
        mdb.createDB();
        start.initConnection();
        Manager manager = new ManagerUserSql(start.connection);
        Gmenu gmenuSession = new GmenuGame(start.connection, in, out);
        start.questionGenToDB();
        List<Mmenu> actions = new ArrayList<>();
        actions.add(new MmNewGame(start.out, gmenuSession));
        actions.add(new MmContinue(start.out, gmenuSession));
        actions.add(new MmExit());
        start.run(actions, in, manager);
    }
}