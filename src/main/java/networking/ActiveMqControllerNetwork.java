package networking;

import controller.ControllerNetwork;

public class ActiveMqControllerNetwork implements ControllerNetwork {
    @Override
    public void send(String identity, String msg) {

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
