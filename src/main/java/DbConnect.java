import org.apache.ibatis.jdbc.ScriptRunner;
import org.postgresql.util.PSQLException;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

public class DbConnect {

    public void createDB() {
            try (InputStream in = DbConnect.class.getClassLoader()
                    .getResourceAsStream("app.properties")) {
                Properties cfg = new Properties();
                cfg.load(in);
                Class.forName(cfg.getProperty("driver-class-name"));
                Connection temp;

                temp = DriverManager.getConnection(
                        cfg.getProperty("url"),
                        cfg.getProperty("username"),
                        cfg.getProperty("password")
                );

                try (PreparedStatement ps = temp.prepareStatement("CREATE DATABASE easyquizy")) {
                    ps.execute();
                    System.out.println("...DB Created");
                    temp = DriverManager.getConnection(
                            cfg.getProperty("urlEQ"),
                            cfg.getProperty("username"),
                            cfg.getProperty("password")
                    );
                }
                ScriptRunner sr = new ScriptRunner(temp);
                Reader reader = new BufferedReader(new FileReader(
                        "src/main/java/sql/tables.sql"));
                sr.runScript(reader);
                reader = new BufferedReader(new FileReader(
                        "src/main/java/sql/topic.sql"));
                sr.runScript(reader);
                moveLines();
            } catch (PSQLException psql) {
                System.out.println("...DB connected");
                moveLines();
            } catch (Exception e) {
                System.out.println("Something went wrong in ManagerDB.createDB()");
                e.printStackTrace();
            }
        }

        private void moveLines() {
            for (int i = 0; i < 21; i++) {
                System.out.println();
            }
        }
}
