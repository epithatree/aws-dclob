package controller;

import java.util.Queue;

public class MessageReader {

    private MessageHistory messageHistory;
    private MessageSender messageSender;
    private SymbolAssignment symbolAssignment;

    private Queue<ControllerMessage> messageQueue;


    public MessageReader(MessageHistory messageHistory,
                         SymbolAssignment symbolAssignment,
                         MessageSender messageSender,
                         Queue<ControllerMessage> messageQueue) {
        this.messageHistory = messageHistory;
        this.symbolAssignment = symbolAssignment;
        this.messageSender = messageSender;
        this.messageQueue = messageQueue;
    }

    public void ReadAndSendNextMessage() {

        ControllerMessage nextMessage = messageQueue.poll();
        if (nextMessage != null) {
            String nextPi = symbolAssignment.addSymbol(nextMessage.getSymbol());
            messageHistory.addMessage(nextMessage.getMessage(), nextMessage.getOrderId(), nextPi);
            messageSender.sendMessage(nextMessage.getMessage(), nextPi);
        }
    }
}
