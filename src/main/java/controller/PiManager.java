package controller;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PiManager {

    private MessageSender messageSender;
    private SymbolAssignment symbolAssignment;
    private MessageHistory messageHistory;
    private List<String> listOfPies;


    public PiManager(MessageSender messageSender, SymbolAssignment symbolAssignment, MessageHistory messageHistory) {
        this.messageSender = messageSender;
        this.symbolAssignment = symbolAssignment;
        this.messageHistory = messageHistory;
        listOfPies = new ArrayList<>();
    }

    private void piUp(String piId) {
        if (!listOfPies.contains(piId)) {
            symbolAssignment.add(piId);
        }
    }

    private void piDown(String piId) {
        if (listOfPies.contains(piId)) {
            String newPi = symbolAssignment.removePi(piId);
            List<String> newOrders = messageHistory.piDown(piId, newPi);
            messageSender.sendMessage(newOrders, newPi);
        }
    }

    private void orderConfirmed(int orderId, String piId) {
        messageHistory.orderCompleted(orderId, piId);
    }

    public void executeMessage(String message) {
        ByteBuffer bb = null;
        int messageType = bb.get();
        int orderId = 0;
        if (messageType == 3) {
            orderId = bb.getInt();
        }
        String piId = bb.asCharBuffer().toString();
        if (messageType == 1) {
            piUp(piId);
        }
        if (messageType == 2) {
            piDown(piId);
        }
        if (messageType == 3) {
            orderConfirmed(orderId, piId);
        }

    }

}
