package thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ThreadCommunicationTest {

    @Test
    public void testThreadCommunication() {

        int stopCondition = 20;
        int[] stats = ThreadCommunication.validateAndPlay(stopCondition, "Test", false);

        Assertions.assertNotNull(stats);
        Assertions.assertEquals(4, stats.length);
        Assertions.assertEquals(stopCondition, stats[0]);
        Assertions.assertEquals(stopCondition, stats[1]);
        Assertions.assertEquals(stopCondition, stats[2]);
        Assertions.assertEquals(stopCondition, stats[3]);
    }

}
