package com.testlang.ast;

import java.util.*;

public class Config {
    private List<ConfigEntry> entries;

    public Config(ConfigEntries entries) {
        this.entries = entries.getEntries();
    }

    public List<ConfigEntry> getEntries() { return entries; }

    public String getBaseUrl() {
        return entries.stream()
                .filter(e -> e.getType().equals("base_url"))
                .map(ConfigEntry::getValue)
                .findFirst()
                .orElse(null);
    }

    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        entries.stream()
                .filter(e -> e.getType().equals("header"))
                .forEach(e -> headers.put(e.getKey(), e.getValue()));
        return headers;
    }
}