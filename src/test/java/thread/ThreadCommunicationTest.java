package thread;

import common.Configuration;
import common.Stats;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ThreadCommunicationTest {

    final int stopCondition = Integer.parseInt(Configuration.DEFAULT_STOP_CONDITION);

    @Test
    public void testThreadCommunication() {

        Stats stats = ThreadCommunication.play(stopCondition, "Test", true);

        Assertions.assertNotNull(stats);
        Assertions.assertEquals(stopCondition, stats.getInitiatorSentMessagesCount());
        Assertions.assertEquals(stopCondition, stats.getInitiatorReceivedMessagesCount());
        Assertions.assertEquals(stopCondition, stats.getPlayerSentMessagesCount());
        Assertions.assertEquals(stopCondition, stats.getPlayerReceivedMessagesCount());
    }

}
