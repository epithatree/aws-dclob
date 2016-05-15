package config;

import controller.*;
import networking.ActiveMqControllerNetwork;
import parser.CsvParser;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ControllerConfig {

    private ControllerNetwork controllerNetwork;
    private SymbolAssignment symbolAssignment;
    private MessageReader messageReader;
    private PiManager piManager;

    private ControllerConfig() {
        this.symbolAssignment = new SymbolAssignment();
        this.controllerNetwork = new ActiveMqControllerNetwork();

        MessageSender messageSender = new MessageSender(controllerNetwork);
        MessageHistory messageHistory = new MessageHistory();
        this.piManager = new PiManager(messageSender, symbolAssignment, messageHistory);

        Queue<ControllerMessage> messageQueue = new LinkedBlockingQueue<>(10);

        this.messageReader = new MessageReader(messageHistory, symbolAssignment, messageSender, messageQueue);
        CsvParser parser = new CsvParser();

        parser.start(messageQueue);
    }

    public static void main(String[] args) {
        ControllerConfig controllerConfig = new ControllerConfig();
        //noinspection InfiniteLoopStatement
        while (true) {
            if (controllerConfig.controllerNetwork.hasMessage()) {
                controllerConfig.piManager.executeMessage(controllerConfig.controllerNetwork.nextMessage());
            } else if (controllerConfig.symbolAssignment.pisAvailable())
                controllerConfig.messageReader.readAndSendNextMessage();
        }
    }

}
