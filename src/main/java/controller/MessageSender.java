package controller;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MessageSender {
    private ControllerNetwork controllerNetwork;

    public MessageSender(ControllerNetwork controllerNetwork) {
        this.controllerNetwork = controllerNetwork;
    }

    void sendMessage(byte[] newOrders, String newPi) {
//		controllerNetwork.send((newOrders), newPi);
        //System.out.println(new String(newPi.toString().getBytes()));
        System.out.println(Arrays.toString(newOrders));
    }


    void sendMessage(List<byte[]> newOrders, String newPi) {
        for (byte[] newOrder : newOrders) {
            sendMessage(newOrder, newPi);
        }

    }


}
