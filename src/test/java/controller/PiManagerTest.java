package controller;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class PiManagerTest {

    private MessageSender messageSender;
    private SymbolAssignment symbolAssignment;
    private MessageHistory messageHistory;

    @Before
    public void setUp() throws Exception {
        messageSender = mock(MessageSender.class);
        symbolAssignment = mock(SymbolAssignment.class);
        messageHistory = mock(MessageHistory.class);
    }

    @Test
    public void testUnableToParseJsonString() throws Exception {
        PiManager piManager = new PiManager(messageSender, symbolAssignment, messageHistory);
        piManager.executeMessage("broken Message");
        verifyZeroInteractions(messageSender);
        verifyZeroInteractions(symbolAssignment);
        verifyZeroInteractions(messageHistory);
    }

    @Test
    public void testNoMessagePresent() throws Exception {
        PiManager piManager = new PiManager(messageSender, symbolAssignment, messageHistory);
        piManager.executeMessage("{}");
        verifyZeroInteractions(messageSender);
        verifyZeroInteractions(symbolAssignment);
        verifyZeroInteractions(messageHistory);
    }

    @Test
    public void testPiUp() throws Exception {
        PiManager piManager = new PiManager(messageSender, symbolAssignment, messageHistory);
        piManager.executeMessage("{\"messageType\":\"PiUp\",\"piId\":\"Pi1\",\"orderId\":1021684525655}");
        verifyZeroInteractions(messageSender);
        verify(symbolAssignment).addPi("Pi1");
        verifyZeroInteractions(messageHistory);
    }

    @Test
    public void testPiAlreadyUp() throws Exception {
        PiManager piManager = new PiManager(messageSender, symbolAssignment, messageHistory);
        piManager.executeMessage("{\"messageType\":\"PiUp\",\"piId\":\"Pi1\",\"orderId\":1021684525655}");
        piManager.executeMessage("{\"messageType\":\"PiUp\",\"piId\":\"Pi1\",\"orderId\":1021684525655}");
        verifyZeroInteractions(messageSender);
        verify(symbolAssignment).addPi("Pi1");
        verifyZeroInteractions(messageHistory);
    }

    @Test
    public void testTooManyMessages() throws Exception {
        PiManager piManager = new PiManager(messageSender, symbolAssignment, messageHistory);
        piManager.executeMessage("{\"messageType\":\"PiUp\",\"piId\":\"Pi1\",\"orderId\":1021684525655}" +
                "{\"messageType\":\"PiUp\",\"piId\":\"Pi1\",\"orderId\":1021684525655}");
        verifyZeroInteractions(messageSender);
        verify(symbolAssignment).addPi("Pi1");
        verifyZeroInteractions(messageHistory);
    }

    @Test
    public void testPiDown() throws Exception {
        ArrayList<String> newOrders = Lists.newArrayList("message");

        when(symbolAssignment.removePi(anyString())).thenReturn("Pi2");
        when(messageHistory.piDown(anyString(), anyString())).thenReturn(newOrders);

        PiManager piManager = new PiManager(messageSender, symbolAssignment, messageHistory);
        piManager.executeMessage("{\"messageType\":\"PiUp\",\"piId\":\"Pi1\",\"orderId\":1021684525655}");
        piManager.executeMessage("{\"messageType\":\"PiDown\",\"piId\":\"Pi1\",\"orderId\":1021684525655}");

        verify(symbolAssignment).removePi("Pi1");
        verify(messageHistory).piDown("Pi1", "Pi2");
        verify(messageSender).sendMessage("Pi2", newOrders);
    }

    @Test
    public void testPiAlreadyDown() throws Exception {
        PiManager piManager = new PiManager(messageSender, symbolAssignment, messageHistory);
        piManager.executeMessage("{\"messageType\":\"PiDown\",\"piId\":\"Pi1\",\"orderId\":1021684525655}");
        verifyZeroInteractions(messageSender);
        verifyZeroInteractions(symbolAssignment);
        verifyZeroInteractions(messageHistory);
    }

    @Test
    public void testOrderConfirm() throws Exception {
        PiManager piManager = new PiManager(messageSender, symbolAssignment, messageHistory);
        piManager.executeMessage("{\"messageType\":\"OrderConfirmed\",\"piId\":\"Pi1\",\"orderId\":1021684525655}");

        verifyZeroInteractions(messageSender);
        verifyZeroInteractions(symbolAssignment);
        verify(messageHistory).orderCompleted("Pi1", 1021684525655L);
    }
}