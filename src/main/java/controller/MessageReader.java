package controller;

import java.util.Queue;

public class MessageReader {

    private MessageHistory messageHistory;
    private MessageSender messageSender;
    private SymbolAssignment symbolAssignment;

    private Queue<MarketDataMessage> messageQueue;


    public MessageReader(MessageHistory messageHistory,
                         SymbolAssignment symbolAssignment,
                         MessageSender messageSender,
                         Queue<MarketDataMessage> messageQueue) {
        this.messageHistory = messageHistory;
        this.symbolAssignment = symbolAssignment;
        this.messageSender = messageSender;
        this.messageQueue = messageQueue;
    }

    public void readAndSendNextMessage() {

        MarketDataMessage nextMessage = messageQueue.poll();
        if (nextMessage != null) {
            String nextPi = symbolAssignment.addSymbol(nextMessage.getSymbol());
            messageHistory.addMessage(nextPi, nextMessage.getOrderId(), nextMessage.getMessage());
            messageSender.sendMessage(nextPi, nextMessage.getMessage());
        }
    }
}
