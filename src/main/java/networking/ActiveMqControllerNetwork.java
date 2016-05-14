package networking;

import controller.ControllerNetwork;

public class ActiveMqControllerNetwork implements ControllerNetwork {
    @Override
    public void send(String msg, String identity) {

    }

    @Override
    public boolean hasMessage() {
        return false;
    }

    @Override
    public String nextMessage() {
        return "";
    }
}
