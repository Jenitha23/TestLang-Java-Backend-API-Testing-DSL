package com.testlang.ast;

public class VariableDeclaration {
    private String name;
    private Object value;

    public VariableDeclaration(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public VariableDeclaration(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() { return name; }
    public Object getValue() { return value; }
}