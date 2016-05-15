package controller;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.*;

@Log4j2
public class PiManager {

    private final MessageSender messageSender;
    private final SymbolAssignment symbolAssignment;
    private final MessageHistory messageHistory;
    private final Set<String> listOfPies;

    private final ObjectReader reader;

    public PiManager(MessageSender messageSender, SymbolAssignment symbolAssignment, MessageHistory messageHistory) {
        this.messageSender = messageSender;
        this.symbolAssignment = symbolAssignment;
        this.messageHistory = messageHistory;
        
        listOfPies = new HashSet<>();

        ObjectMapper objectMapper = new ObjectMapper();
        reader = objectMapper.readerFor(NetworkControlMessage.class);
    }

    public void executeMessage(String rawMessage) {
        Optional<NetworkControlMessage> optionalMessage = parseMessage(rawMessage);
        if (!optionalMessage.isPresent()) {
            log.warn("Unable to process message");
            return;
        }
        NetworkControlMessage message = optionalMessage.get();

        String piId = message.getPiId();
        switch (message.getMessageType()) {
            case PiUp:
                piUp(piId);
                break;
            case PiDown:
                piDown(piId);
                break;
            case OrderConfirmed:
                orderConfirmed(piId, message.getOrderId());
                break;
        }
    }

    private Optional<NetworkControlMessage> parseMessage(String message) {
        MappingIterator<NetworkControlMessage> mappingIterator = null;
        try {
            NetworkControlMessage networkControlMessage = null;
            mappingIterator = reader.readValues(message);
            if (mappingIterator.hasNext()) {
                networkControlMessage = mappingIterator.next();
            }
            while (mappingIterator.hasNext()) {
                log.warn("Ignoring extra message contained in input string {}", mappingIterator.next());
            }
            if (networkControlMessage == null) {
                log.warn("Message did not contain any network control messages. {}", message);
                return Optional.empty();
            }
            return Optional.of(networkControlMessage);
        } catch (IOException e) {
            log.error("Unable to parse json: {}", message);
            return Optional.empty();
        }
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
            messageSender.sendMessage(newPi, newOrders);
        }
    }

    private void orderConfirmed(String piId, Long orderId) {
        messageHistory.orderCompleted(piId, orderId);
    }

}
