package com.testlang.ast;

import java.util.*;

public class Tests {
    private List<TestBlock> tests = new ArrayList<>();

    public void addTest(TestBlock test) {
        tests.add(test);
    }

    public List<TestBlock> getTests() { return tests; }
}