package com.testlang.ast;

public class ConfigEntry {
    private String type;
    private String key;
    private String value;

    public ConfigEntry(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public ConfigEntry(String type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public String getType() { return type; }
    public String getKey() { return key; }
    public String getValue() { return value; }
}