package process;

import common.Configuration;

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

        int[] stats = ProcessCommunicationServer.validateAndPlay(stopCondition, false, port, initialMessage);

        Assertions.assertNotNull(stats);
        Assertions.assertEquals(2, stats.length);
        Assertions.assertEquals(stopCondition, stats[0]);
        Assertions.assertEquals(stopCondition, stats[1]);
    }


}
