package controller;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class SymbolAssignmentTest {

    @Test
    public void testNoPisAvailable() throws Exception {
        SymbolAssignment symbolAssignment = new SymbolAssignment();
        assertThat(symbolAssignment.pisAvailable(), is(false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSymbolBeforePisAvailableThrowsException() throws Exception {
        SymbolAssignment symbolAssignment = new SymbolAssignment();
        symbolAssignment.addSymbol(1);
    }

    @Test(expected = IllegalStateException.class)
    public void testRemovePisWhenPoolIsEmptyThrowsException() throws Exception {
        SymbolAssignment symbolAssignment = new SymbolAssignment();
        symbolAssignment.removePi("Pi1");
    }

    @Test(expected = IllegalStateException.class)
    public void testRemovePisWhenPoolHasOnePiThrowsException() throws Exception {
        SymbolAssignment symbolAssignment = new SymbolAssignment();
        symbolAssignment.addPi("Pi1");
        symbolAssignment.removePi("Pi1");
    }

    @Test
    public void testAddingSymbolReturnsIdOfPiInPool() throws Exception {
        SymbolAssignment symbolAssignment = new SymbolAssignment();
        symbolAssignment.addPi("Pi1");
        assertThat(symbolAssignment.addSymbol(1), is("Pi1"));
    }

    @Test
    public void testAddingSymbolTwiceReturnsSamePi() throws Exception {
        SymbolAssignment symbolAssignment = new SymbolAssignment();
        symbolAssignment.addPi("Pi1");
        symbolAssignment.addPi("Pi2");
        assertThat(symbolAssignment.addSymbol(1), is("Pi1"));
        assertThat(symbolAssignment.addSymbol(1), is("Pi1"));
    }

    @Test
    public void testAddingDifferentSymbolsReturnsDifferentPis() throws Exception {
        SymbolAssignment symbolAssignment = new SymbolAssignment();
        symbolAssignment.addPi("Pi1");
        symbolAssignment.addPi("Pi2");
        assertThat(symbolAssignment.addSymbol(1), is("Pi1"));
        assertThat(symbolAssignment.addSymbol(2), is("Pi2"));
    }

    @Test
    public void testRemovingPiMovesSymbolToOtherPi() throws Exception {
        SymbolAssignment symbolAssignment = new SymbolAssignment();
        symbolAssignment.addPi("Pi1");
        symbolAssignment.addPi("Pi2");
        assertThat(symbolAssignment.addSymbol(1), is("Pi1"));
        assertThat(symbolAssignment.addSymbol(2), is("Pi2"));
        assertThat(symbolAssignment.removePi("Pi2"), is("Pi1"));
        assertThat(symbolAssignment.addSymbol(2), is("Pi1"));
    }

    @Test
    public void testAddingSymbolTwiceReturnsSamePiWithThrePis() throws Exception {
        SymbolAssignment symbolAssignment = new SymbolAssignment();
        symbolAssignment.addPi("Pi1");
        symbolAssignment.addPi("Pi2");
        symbolAssignment.addPi("Pi3");
        assertThat(symbolAssignment.addSymbol(1), is("Pi1"));
        assertThat(symbolAssignment.addSymbol(1), is("Pi1"));
    }

    @Test
    public void testAddingDifferentSymbolsReturnsDifferentPisWithThreePis() throws Exception {
        SymbolAssignment symbolAssignment = new SymbolAssignment();
        symbolAssignment.addPi("Pi1");
        symbolAssignment.addPi("Pi2");
        symbolAssignment.addPi("Pi3");
        assertThat(symbolAssignment.addSymbol(1), is("Pi1"));
        assertThat(symbolAssignment.addSymbol(2), not("Pi1"));
    }

    @Test
    public void testRemovingPiMovesSymbolToOtherPiWithThreePis() throws Exception {
        SymbolAssignment symbolAssignment = new SymbolAssignment();
        symbolAssignment.addPi("Pi1");
        symbolAssignment.addPi("Pi2");
        symbolAssignment.addPi("Pi3");
        assertThat(symbolAssignment.addSymbol(1), is("Pi1"));
        assertThat(symbolAssignment.addSymbol(2), not("Pi1"));

        String secondPi = symbolAssignment.addSymbol(2);

        String newPi = symbolAssignment.removePi(secondPi);

        assertThat(symbolAssignment.addSymbol(2), is(newPi));
    }
}