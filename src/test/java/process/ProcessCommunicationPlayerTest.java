package process;

import common.Configuration;
import common.Stats;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static common.Configuration.KEY_PORT_NUMBER;
import static common.Configuration.KEY_STOP_CONDITION;

@Order(2)
public class ProcessCommunicationPlayerTest {

    // make sure that ProcessCommunicationServerTest runs before ProcessCommunicationClientTest !
    // make sure that stopCondition and port are same in both server and client !
    final int stopCondition = Integer.parseInt(Configuration.read(KEY_STOP_CONDITION));
    final int port = Integer.parseInt(Configuration.read(KEY_PORT_NUMBER));

    @Test
    public void testProcessCommunicationClient() {

        Stats stats = ProcessCommunicationClient.play(stopCondition, true, port);
        Assertions.assertNotNull(stats);
        Assertions.assertEquals(stopCondition, stats.getPlayerSentMessagesCount());
        Assertions.assertEquals(stopCondition, stats.getPlayerReceivedMessagesCount());
    }

}
