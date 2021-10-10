public class MmContinue implements Mmenu {

    private final Output out;
    private final Gmenu gm;

    public MmContinue(Output out, Gmenu gm) {
        this.out = out;
        this.gm = gm;
    }

    @Override
    public String name() {
        return ">>> Continue.";
    }

    @Override
    public boolean execute(Input input, Manager manager) {
        for (ClassUser classUser : manager.getUserList()) {
            System.out.println(classUser);
        }
        String name = input.askStr("Who are you today?");
        ClassUser user = manager.getUserByName(name);
        if (user != null) {
            gm.printGame(user);
        }
        out.println("Seems like you have never played before. Should you create a new game?");
        return true;
    }
}
