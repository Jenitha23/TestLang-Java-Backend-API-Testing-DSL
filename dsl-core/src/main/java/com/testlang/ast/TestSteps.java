package com.testlang.ast;

import java.util.*;

public class TestSteps {
    private List<TestStep> steps = new ArrayList<>();

    public void addStep(TestStep step) {
        steps.add(step);
    }

    public List<TestStep> getSteps() { return steps; }
}