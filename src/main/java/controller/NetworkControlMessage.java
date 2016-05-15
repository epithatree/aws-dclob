package controller;

public class NetworkControlMessage {
    private NetworkControlMessageType messageType;
    private String piId;
    private long orderId;

    public NetworkControlMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = NetworkControlMessageType.valueOf(messageType);
    }

    public String getPiId() {
        return piId;
    }

    public void setPiId(String piId) {
        this.piId = piId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "NetworkControlMessage{" +
                "messageType=" + messageType +
                ", piId='" + piId + '\'' +
                ", orderId=" + orderId +
                '}';
    }
}
