package parser;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"timeStamp", "messageType", "orderId", "size", "price", "direction"})
public class Message {
    private double timeStamp;
    private MessageType messageType;
    private long orderId;
    private int size;
    private long price;
    private Direction direction;


    public double getTimeStamp() {
        return timeStamp;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public long getOrderId() {
        return orderId;
    }

    public int getSize() {
        return size;
    }

    public long getPrice() {
        return price;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setTimeStamp(double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMessageType(int messageType) {
        this.messageType = parseMessageType(messageType);
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setDirection(int direction) {
        this.direction = parseDirection(direction);
    }

    private MessageType parseMessageType(int messageType) {
        return MessageTypeUtils.parseMessageType(messageType);
    }

    private Direction parseDirection(int direction) {
        if (direction == -1) {
            return Direction.Sell;
        } else if (direction == 1) {
            return Direction.Buy;
        }
        throw new IllegalArgumentException(String.format("Direction must be either 1 or -1, was %s", direction));
    }

    @Override
    public String toString() {
        return "Message{" +
                "timeStamp=" + timeStamp +
                ", messageType=" + messageType +
                ", orderId=" + orderId +
                ", size=" + size +
                ", price=" + price +
                ", direction=" + direction +
                '}';
    }
}
