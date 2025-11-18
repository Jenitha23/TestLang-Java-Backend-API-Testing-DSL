package com.testlang.ast;

import java.util.*;

public class RequestBlock {
    private List<RequestEntry> entries;

    public RequestBlock() {
        this.entries = new ArrayList<>();
    }

    public RequestBlock(RequestEntries entries) {
        this.entries = entries.getEntries();
    }

    public List<RequestEntry> getEntries() { return entries; }

    public String getBody() {
        return entries.stream()
                .filter(e -> e.getType().equals("body"))
                .map(RequestEntry::getValue)
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