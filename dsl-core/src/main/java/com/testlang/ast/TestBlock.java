package com.testlang.ast;

public class TestBlock {
    private String name;
    private TestSteps steps;

    public TestBlock(String name, TestSteps steps) {
        this.name = name;
        this.steps = steps;
    }

    public String getName() { return name; }
    public TestSteps getSteps() { return steps; }
}