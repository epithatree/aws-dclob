package networking;

import controller.ControllerNetwork;
import lombok.extern.log4j.Log4j2;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

@Log4j2
public class ActiveMqControllerNetwork implements ControllerNetwork {

    private final ActiveMQConnectionFactory connectionFactory;
    private final Map<String, MessageProducer> producerMap;
    private final Queue<String> messageQueue;

    private Session session;

    public ActiveMqControllerNetwork() {
        connectionFactory = new ActiveMQConnectionFactory(
                "http://ec2-54-186-32-210.us-west-2.compute.amazonaws.com/");
        producerMap = new HashMap<>();
        messageQueue = new LinkedList<>();
    }

    public void start() {
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic("CONTROLLER");
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        messageQueue.add(textMessage.getText());
                    } catch (JMSException e) {
                        log.error("Failed to read message", e);
                    }
                } else {
                    log.warn("Received non text message {}", message);
                }
            });
        } catch (JMSException e) {
            log.error("Unable to start activeMq connection", e);
        }
    }

    @Override
    public void send(String identity, String msg) {
        MessageProducer producer = producerMap.get(identity);
        if (producer == null) {
            producer = getProducer(identity);
            if (producer == null) {
                return;
            }
        }
        producerMap.put(identity, producer);
        try {
            TextMessage message = session.createTextMessage(msg);
            producer.send(message);
        } catch (JMSException e) {
            log.error("Failed to send message for {} with message {}", identity, msg, e);
        }
    }

    @Override
    public boolean hasMessage() {
        return !messageQueue.isEmpty();
    }

    @Override
    public String nextMessage() {
        return messageQueue.remove();
    }

    private MessageProducer getProducer(String identity) {
        try {
            Destination destination = session.createTopic(identity);
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            return producer;
        } catch (JMSException e) {
            log.error("Failed to create topic for {}", identity, e);
        }
        return null;
    }
}
