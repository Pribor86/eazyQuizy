public class MmNewGame implements Mmenu {

    private final Output out;
    private final Gmenu gm;

    public MmNewGame(Output out, Gmenu gm) {
        this.out = out;
        this.gm = gm;
    }

    @Override
    public String name() {
        return ">>> Start new game.";
    }

    @Override
    public boolean execute(Input input, Manager manager) {
        String name = input.askStr("What is your name buddy?");
        ClassUser classUser = new ClassUser(name);
        boolean isNew = manager.createUser(classUser);
        if (isNew) {
            out.println(System.lineSeparator());
            out.println("Welcome to our quiz session, " + name + "!");
            out.println("Let's start the game!");
            out.println(System.lineSeparator());
            gm.printGame(classUser);
        }
        return true;
    }
}
