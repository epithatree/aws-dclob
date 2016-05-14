package networking;

import controller.ControllerNetwork;

public class ActiveMqControllerNetwork implements ControllerNetwork {
    @Override
    public boolean hasMessage() {
        return false;
    }

    @Override
    public byte[] nextMessage() {
        return new byte[0];
    }
}
