package controller;

import java.util.List;

public class MessageSender {
    private ControllerNetwork controllerNetwork;

    public MessageSender(ControllerNetwork controllerNetwork) {
        this.controllerNetwork = controllerNetwork;
    }

    void sendMessage(String newPi, String newOrders) {
        controllerNetwork.send(newPi, newOrders);
    }

    void sendMessage(String newPi, List<String> newOrders) {
        for (String newOrder : newOrders) {
            sendMessage(newPi, newOrder);
        }
    }
}
