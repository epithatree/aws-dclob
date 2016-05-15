package controller;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        MappingIterator<NetworkControlMessage> mappingIterator;
        try {
            NetworkControlMessage networkControlMessage = null;
            mappingIterator = reader.readValues(message);
            if (mappingIterator.hasNext()) {
                networkControlMessage = mappingIterator.next();
            }
            while (mappingIterator.hasNext()) {
                log.warn("Ignoring extra message contained in input string {}", mappingIterator.next());
            }
            if (networkControlMessage == null || networkControlMessage.getMessageType() == null) {
                log.warn("Message did not contain any network control messages. {}", message);
                return Optional.empty();
            }
            return Optional.of(networkControlMessage);
        } catch (IOException | RuntimeJsonMappingException e) {
            log.error("Unable to parse json: {}", message);
            return Optional.empty();
        }
    }

    private void piUp(String piId) {
        if (!listOfPies.contains(piId)) {
            listOfPies.add(piId);
            symbolAssignment.addPi(piId);
        } else {
            log.warn("Received PiUp notification for {}, that pi was already listed as running.", piId);
        }
    }

    private void piDown(String piId) {
        if (listOfPies.contains(piId)) {
            log.warn("{} has gone down", piId);
            listOfPies.remove(piId);
            String newPi = symbolAssignment.removePi(piId);
            List<String> newOrders = messageHistory.piDown(piId, newPi);
            messageSender.sendMessage(newPi, newOrders);
        } else {
            log.warn("Received PiDown notification for {}, that pi was not listed as running.", piId);
        }
    }

    private void orderConfirmed(String piId, Long orderId) {
        messageHistory.orderCompleted(piId, orderId);
    }

}
