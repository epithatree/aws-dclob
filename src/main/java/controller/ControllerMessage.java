package controller;


public class ControllerMessage {
    private final byte[] message;
    private final int orderId;
    private final int symbol;

    public ControllerMessage(byte[] message, int orderId, int symbol) {
        this.message = message;
        this.orderId = orderId;
        this.symbol = symbol;
    }


    byte[] getMessage() {
        return message;
    }

    int getOrderId() {
        return orderId;
    }

    int getSymbol() {
        return symbol;
    }
}
