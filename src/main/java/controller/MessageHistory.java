package controller;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

public class MessageHistory {

    private Map<String, Multimap<Long, String>> history; //List of messages for each order id, list of order ids for each pi, list of pies

    public MessageHistory() {
        history = new HashMap<>();
    }

    void addMessage(String pi, Long orderID, String message) {
        Multimap<Long, String> piMap = history.get(pi); // checks to see if any messages have been sent to this particular pi before
        if (piMap == null) {
            Multimap<Long, String> newMap = ArrayListMultimap.create();
            newMap.put(orderID, message);
            history.put(pi, newMap);
        } else {
            piMap.put(orderID, message);
        }
    }

    void orderCompleted(String piId, Long orderId) {
        Multimap<Long, String> piMap = history.get(piId);
        if (piMap == null || piMap.get(orderId).isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("No messages associated with orderId, %s %s", piId, orderId));
        }
        piMap.removeAll(orderId);
    }

    List<String> piDown(String oldPiId, String newPiId) {
        List<String> listOfMessagesToBeSent = new LinkedList<>();

        Multimap<Long, String> oldPiMap = history.get(oldPiId);

        if (oldPiMap == null) {
            return listOfMessagesToBeSent;
        }
        Multimap<Long, String> newPiMap = history.get(newPiId);
        if (newPiMap == null) {
            Multimap<Long, String> newMap = ArrayListMultimap.create();
            history.put(newPiId, newMap);
            newPiMap = newMap;
        }

        for (Long currentOldPiElement : oldPiMap.keySet()) {
            Collection<String> currentOldPiMessages = oldPiMap.get(currentOldPiElement); //Generate a list of all the messages associated with a particular orderID
            listOfMessagesToBeSent.addAll(currentOldPiMessages); //Add these messages to the output
            newPiMap.putAll(currentOldPiElement, currentOldPiMessages); //Add these orderID and associated messages to the new pis
        }
        return listOfMessagesToBeSent;
    }

}
