package process;

import common.Configuration;
import common.Stats;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static common.Configuration.KEY_PORT_NUMBER;
import static common.Configuration.KEY_STOP_CONDITION;

@Order(1)
public class ProcessCommunicationInitiatorTest {

    // make sure that ProcessCommunicationServerTest runs before ProcessCommunicationClientTest !
    // make sure that stopCondition and port are same in both server and client !
    final int stopCondition = Integer.parseInt(Configuration.read(KEY_STOP_CONDITION));
    final int port = Integer.parseInt(Configuration.read(KEY_PORT_NUMBER));


    @Test
    public void testProcessCommunicationServer() {

        String initialMessage = "Test";

        Stats stats = ProcessCommunicationServer.play(stopCondition, true, port, initialMessage);
        Assertions.assertNotNull(stats);
        Assertions.assertEquals(stopCondition, stats.getInitiatorSentMessagesCount());
        Assertions.assertEquals(stopCondition, stats.getInitiatorReceivedMessagesCount());
    }


}
