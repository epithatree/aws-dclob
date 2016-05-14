package parser;

public class MessageTypeUtils {

    public static MessageType parseMessageType(int messageTypeInt) {
        switch (messageTypeInt) {
            case 1: {
                return MessageType.Submission;
            }
            case 2: {
                return MessageType.Cancellation;
            }
            case 3: {
                return MessageType.Deletion;
            }
            case 4: {
                return MessageType.VisibleExecution;
            }
            case 5: {
                return MessageType.HiddenExecution;
            }
            case 7: {
                return MessageType.TradingHalt;
            }
            default: {
                return MessageType.Invalid;
            }
        }
    }


}
