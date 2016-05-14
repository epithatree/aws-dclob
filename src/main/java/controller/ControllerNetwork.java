package controller;

public interface ControllerNetwork {

    void send(String msg, String identity);

    boolean hasMessage();

    String nextMessage();
}
