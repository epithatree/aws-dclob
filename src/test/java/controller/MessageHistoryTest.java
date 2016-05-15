package controller;

import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MessageHistoryTest {

    @Test
    public void testOneMessageIsRetrievedOnPiDown() throws Exception {
        MessageHistory messageHistory = new MessageHistory();

        messageHistory.addMessage("pi1", 123L, "message1");
        assertThat(messageHistory.piDown("pi1", "pi2"), is(newArrayList("message1")));
    }

    @Test
    public void testTwoMessagesAreRetrievedInOrderOnPiDown() throws Exception {
        MessageHistory messageHistory = new MessageHistory();

        messageHistory.addMessage("pi1", 123L, "message1");
        messageHistory.addMessage("pi1", 123L, "message2");

        assertThat(messageHistory.piDown("pi1", "pi2"), is(newArrayList("message1", "message2")));
    }

    @Test
    public void testOnlyMessageAssociatedWithCorrectPiAreReturned() throws Exception {
        MessageHistory messageHistory = new MessageHistory();

        messageHistory.addMessage("pi1", 123L, "message1");
        messageHistory.addMessage("pi2", 123L, "message2");

        assertThat(messageHistory.piDown("pi1", "pi2"), is(newArrayList("message1")));
    }

    @Test
    public void testMessagesAreRemovedOnOrderComplete() throws Exception {
        MessageHistory messageHistory = new MessageHistory();

        messageHistory.addMessage("pi1", 123L, "message1");
        messageHistory.addMessage("pi1", 124L, "message2");

        messageHistory.orderCompleted("pi1", 123L);

        assertThat(messageHistory.piDown("pi1", "pi2"), is(newArrayList("message2")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPiForOrderCompletedDoesNotExist() throws Exception {
        MessageHistory messageHistory = new MessageHistory();
        messageHistory.orderCompleted("pi1", 123L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOrderIdThrowsException() throws Exception {
        MessageHistory messageHistory = new MessageHistory();
        messageHistory.addMessage("pi1", 123L, "message1");
        messageHistory.orderCompleted("pi1", 123L);
        messageHistory.orderCompleted("pi1", 123L);
    }
}