package controller;

public interface ControllerNetwork {

    void send(String identity, String msg);

    boolean hasMessage();

    String nextMessage();
}
