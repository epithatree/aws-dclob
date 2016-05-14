package controller;


public class ControllerMessage {
    private final String message;
    private final long orderId;
    private final int symbol;

    public ControllerMessage(String message, long orderId, int symbol) {
        this.message = message;
        this.orderId = orderId;
        this.symbol = symbol;
    }


    String getMessage() {
        return message;
    }

    long getOrderId() {
        return orderId;
    }

    int getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "ControllerMessage{" +
                "message='" + message + '\'' +
                ", orderId=" + orderId +
                ", symbol=" + symbol +
                '}';
    }
}
