package com.testlang.ast;

import java.util.*;

public class RequestEntries {
    private List<RequestEntry> entries = new ArrayList<>();

    public void addEntry(RequestEntry entry) {
        entries.add(entry);
    }

    public List<RequestEntry> getEntries() { return entries; }
}