package common;

public class Stats {

    int initiatorSentMessagesCount;
    int initiatorReceivedMessagesCount;
    int playerSentMessagesCount;
    int playerReceivedMessagesCount;

    public int getInitiatorSentMessagesCount() {
        return initiatorSentMessagesCount;
    }

    public void setInitiatorSentMessagesCount(int initiatorSentMessagesCount) {
        this.initiatorSentMessagesCount = initiatorSentMessagesCount;
    }

    public int getInitiatorReceivedMessagesCount() {
        return initiatorReceivedMessagesCount;
    }

    public void setInitiatorReceivedMessagesCount(int initiatorReceivedMessagesCount) {
        this.initiatorReceivedMessagesCount = initiatorReceivedMessagesCount;
    }

    public int getPlayerReceivedMessagesCount() {
        return playerReceivedMessagesCount;
    }

    public void setPlayerReceivedMessagesCount(int playerReceivedMessagesCount) {
        this.playerReceivedMessagesCount = playerReceivedMessagesCount;
    }

    public int getPlayerSentMessagesCount() {
        return playerSentMessagesCount;
    }

    public void setPlayerSentMessagesCount(int playerSentMessagesCount) {
        this.playerSentMessagesCount = playerSentMessagesCount;
    }
}
