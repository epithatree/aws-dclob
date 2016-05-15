package controller;

import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Log4j2
class PiStatus implements Comparable<PiStatus> {
    private final String id;
    private Set<Integer> assignedSymbols;

    PiStatus(String id) {
        this.id = id;
        assignedSymbols = new HashSet<>();
    }

    @Override
    public int compareTo(PiStatus that) {
        return Integer.compare(this.assignedSymbols.size(), that.assignedSymbols.size());
    }

    void addSymbol(Integer symbol) {
        if (assignedSymbols.contains(symbol)) {
            log.warn("Symbol {} has already been assigned to this pi", symbol);
        } else {
            assignedSymbols.add(symbol);
        }
    }

    String getId() {
        return id;
    }

    Set<Integer> getAssignedSymbols() {
        return Collections.unmodifiableSet(assignedSymbols);
    }
}
