package controller;

import java.util.List;

public class MessageSender {
    private ControllerNetwork controllerNetwork;

    public MessageSender(ControllerNetwork controllerNetwork) {
        this.controllerNetwork = controllerNetwork;
    }

    void sendMessage(String newOrders, String newPi) {
        controllerNetwork.send((newOrders), newPi);
    }

    void sendMessage(List<String> newOrders, String newPi) {
        for (String newOrder : newOrders) {
            sendMessage(newOrder, newPi);
        }
    }
}
