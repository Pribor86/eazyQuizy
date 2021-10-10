public class GmenuSession implements Gmenu {
    public void printGame(ClassUser user) {
        try {
            System.out.println(
                    "Hey " + user.getName() + ", The game is started!"
            + System.lineSeparator());
            Thread.sleep(5000);
            System.out.println(
                    "Actually this game still in beta. So far you can use your imagination."
                            + System.lineSeparator());
            Thread.sleep(5000);
            System.out.println(
                    "At this stage the game is completely wireless and require nothing "
                            + "but your imagination."
                            + System.lineSeparator());
            Thread.sleep(4000);
            System.out.println(
                    "But be careful and do not overthink. We still going to impress you."
                            + System.lineSeparator());
            Thread.sleep(3000);
            System.out.println(
                    "Some time later..."
                            + System.lineSeparator());
            Thread.sleep(7500);
            System.out.println(
                    "How is your game so far?"
                            + System.lineSeparator());
            Thread.sleep(4000);
            System.out.println(
                    "I wish I can join your game " + user.getName() + "."
                            + System.lineSeparator());
            Thread.sleep(7500);
            System.out.println(
                    "All right " + user.getName() + ", your demo time is expired =)."
                            + System.lineSeparator());
            Thread.sleep(4500);
            System.out.println(
                    "Just kidding, keep having fun."
                            + System.lineSeparator());
            Thread.sleep(4000);
            System.out.println(
                    "By the way, do you have any friends?"
                            + System.lineSeparator());
            Thread.sleep(4500);
            System.out.println(
                    "You know sitting here and reading pre-programmed conversation "
                            + "between me and you..."
                            + System.lineSeparator());
            Thread.sleep(3000);
            System.out.println("...kind of a bit suspicious."
                    + System.lineSeparator());
            Thread.sleep(3000);
            System.out.println(
                    "Should you go outside for a walk may-yyybe?"
                            + System.lineSeparator());
            Thread.sleep(6000);
            System.out.println(
                    "Ok, that's it my friend. Termination of the process."
                            + System.lineSeparator());
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    @Override
    public void getList() {
    }

    @Override
    public void getQuestionList() {

    }

    @Override
    public void close() throws Exception {
    }
}
