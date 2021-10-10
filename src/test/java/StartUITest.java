import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StartUITest {

    @Test
    public void whenInputIsNegative() {
        Input in = new InputDumb(new String[] {"-1"});

        int select = in.askInt("Enter: ");
        assertThat(select, is(-1));
    }
}