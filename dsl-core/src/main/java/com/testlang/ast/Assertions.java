package com.testlang.ast;

import java.util.*;

public class Assertions {
    private List<Assertion> assertions = new ArrayList<>();

    public void addAssertion(Assertion assertion) {
        assertions.add(assertion);
    }

    public List<Assertion> getAssertions() { return assertions; }
}