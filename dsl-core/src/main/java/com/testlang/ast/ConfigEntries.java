package com.testlang.ast;

import java.util.*;

public class ConfigEntries {
    private List<ConfigEntry> entries = new ArrayList<>();

    public void addEntry(ConfigEntry entry) {
        entries.add(entry);
    }

    public List<ConfigEntry> getEntries() { return entries; }
}