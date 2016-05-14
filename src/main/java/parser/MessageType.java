package parser;

public enum MessageType {
    Submission(1),
    Cancellation(2),
    Deletion(3),
    VisibleExecution(4),
    HiddenExecution(5),
    TradingHalt(7),
    Invalid(-100);

    private int type;

    MessageType(int messageTypeInt) {
        this.type = messageTypeInt;
    }

    public int getType() {
        return type;
    }

}
