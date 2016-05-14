package controller;

import java.util.*;

public class MessageHistory {

    private MessageSender messageSender;
    private ListOfPi history; //List of messages for each order id, list of order ids for each pi, list of pies

    public MessageHistory(MessageSender messageSender) {
        this.messageSender = messageSender;
        history = new ListOfPi();
    }

    void addMessage(String message, Long orderID, String pi) {
        Pi newPi = history.get(pi); // checks to see if any messages have been sent to this particular pi before
        if (newPi != null) {
            if (newPi.listOfOrderID.containsKey(orderID)) { //checks to see if that orderid has been seen before
                newPi.listOfOrderID.get(orderID).add(message); //adds the message to the list associtated with that orderid
            } else {
                LinkedList<String> newMessageList = new LinkedList<>();
                newMessageList.add(message);
                newPi.listOfOrderID.put(orderID, newMessageList); //Creates a new linked list with one element which is the message to be added
            }
        } else history.listOfPi = new ArrayList<>();
    }

    void orderCompleted(int orderId, String piId) {
        history.get(piId).listOfOrderID.remove(orderId);
    }

    List<String> piDown(String piId, String newPi2) {
        List<String> listOfMessagesToBeSent = new LinkedList<>();

        Pi oldPi = history.get(piId);
        Pi newPi = history.get(newPi2);

        Iterator<Long> oldPiElements = oldPi.listOfOrderID.keySet().iterator();
        while (oldPiElements.hasNext()) {
            Long currentOldPiElement = oldPiElements.next(); //iterate through the elements in the hash table associated with the pi that has just gone down
            Queue<String> currentOldPiMessage = oldPi.listOfOrderID.get(currentOldPiElement); //Generate a list of all the messages associated with a particular orderID
            listOfMessagesToBeSent.addAll(currentOldPiMessage); //Add these messages to the output
            newPi.listOfOrderID.put(currentOldPiElement, currentOldPiMessage); //Add these orderID and associated messages to the new pis
        }


        return listOfMessagesToBeSent;
    }

    private class Pi {
        String name;
        HashMap<Long, Queue<String>> listOfOrderID; //List of OrderIDs

    }

    private class ListOfPi {
        List<Pi> listOfPi;

        ListOfPi() {
            listOfPi = new ArrayList<>(10);
        }

        Pi get(String piId) {
            for (Pi aListOfPi : listOfPi) {
                if (aListOfPi.name == piId) return aListOfPi;
            }
            return null;
        }
    }


}
