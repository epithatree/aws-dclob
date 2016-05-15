package controller;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

@Log4j2
public class SymbolAssignment {

    private final PriorityQueue<PiStatus> piFrequencyList; //Queue of Pis ordered by number of symbols assigned to them
    private final Map<Integer, PiStatus> symbolMap;
    //Least loaded pi is at the head of the list

    private final Map<String, PiStatus> idMap;

    public SymbolAssignment() {
        piFrequencyList = new PriorityQueue<>();
        symbolMap = new HashMap<>();
        idMap = new HashMap<>();
    }

    public boolean pisAvailable() {
        return !piFrequencyList.isEmpty();
    }

    void addPi(String piId) { //Adds a pi to the pool available to have symbols assigned to them
        PiStatus newPiStatus = new PiStatus(piId);
        piFrequencyList.add(newPiStatus);
        idMap.put(piId, newPiStatus);
        log.info("Added new pi {} to the pool", piId);
    }

    String removePi(String piId) { //Removes a pi from the pool, moving all assigned symbols to a new pi, if possible
        if (piFrequencyList.isEmpty()) {
            throw new IllegalStateException("Attempting to remove pi, but pool is empty");
        }
        if (piFrequencyList.size() == 1) {
            throw new IllegalStateException("All pis have gone down");
        }
        PiStatus piStatus = idMap.remove(piId); //Remove old pi status from the id map
        piFrequencyList.remove(piStatus); //Remove old pi status from the frequency list
        PiStatus newPiStatus = piFrequencyList.remove(); //Get replacement pi status
        piStatus.getAssignedSymbols().forEach(symbol -> {
            newPiStatus.addSymbol(symbol);
            symbolMap.put(symbol, newPiStatus);
        }); //Assign all symbols to the new pi
        piFrequencyList.add(newPiStatus); //Add new pi status to the frequency list
        return newPiStatus.getId();
    }

    String addSymbol(int symbol) { //returns id of a pi
        if (piFrequencyList.isEmpty()) {
            throw new IllegalStateException("No Pis currently available");
        }
        PiStatus piStatus = symbolMap.get(symbol);
        if (piStatus != null) { //If the symbol has been seen before, returns the pi associated with that symbol
            return piStatus.getId();
        } else { //Assigns the symbol to the pi with the lowest number of symbols associated with it and returns the pi
            piStatus = piFrequencyList.remove();
            piStatus.addSymbol(symbol);
            piFrequencyList.add(piStatus);
            symbolMap.put(symbol, piStatus);
            String piId = piStatus.getId();
            log.info("Assigning symbol {} to pi {}", symbol, piId);
            return piId;
        }
    }

}
