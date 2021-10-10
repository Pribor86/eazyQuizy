public class MmExit implements Mmenu {
    @Override
    public String name() {
        return ">>> Exit.";
    }

    @Override
    public boolean execute(Input input, Manager manager) {
        return false;
    }
}
