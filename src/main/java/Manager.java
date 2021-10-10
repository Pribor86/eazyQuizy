import java.util.List;

public interface Manager extends AutoCloseable {

    public boolean createUser(ClassUser classUser);

    public boolean updateUser(ClassUser classUser);

    public List<ClassUser> getUserList();

    public ClassUser getUserByName(String name);
}
