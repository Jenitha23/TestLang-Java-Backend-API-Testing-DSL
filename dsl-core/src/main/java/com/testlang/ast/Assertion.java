package com.testlang.ast;

public class Assertion {
    private String type;
    private String key;
    private Object value;

    public Assertion(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Assertion(String type, String key, Object value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public String getType() { return type; }
    public String getKey() { return key; }
    public Object getValue() { return value; }
}