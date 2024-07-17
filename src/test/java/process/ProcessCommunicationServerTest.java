package process;

import common.Configuration;
import common.Stats;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProcessCommunicationServerTest {

    // make sure that ProcessCommunicationServerTest runs before ProcessCommunicationClientTest !
    // make sure that stopCondition and port are same in both server and client !
    int stopCondition = Integer.parseInt(Configuration.DEFAULT_STOP_CONDITION);
    int port = Integer.parseInt(Configuration.DEFAULT_PORT_NUMBER);

    @Test
    public void testProcessCommunicationServer() {

        String initialMessage = "Test";

        Stats stats = ProcessCommunicationServer.play(stopCondition, true, port, initialMessage);
        Assertions.assertNotNull(stats);
        Assertions.assertEquals(stopCondition, stats.getInitiatorSentMessagesCount());
        Assertions.assertEquals(stopCondition, stats.getInitiatorReceivedMessagesCount());
    }


}
