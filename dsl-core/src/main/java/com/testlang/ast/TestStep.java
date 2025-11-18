package com.testlang.ast;

public class TestStep {
    private Request request;
    private Assertion assertion;

    // Constructor for request steps
    public TestStep(Request request) {
        this.request = request;
        this.assertion = null;
    }

    // Constructor for assertion steps
    public TestStep(Assertion assertion) {
        this.request = null;
        this.assertion = assertion;
    }

    public boolean isRequest() { return request != null; }
    public boolean isAssertion() { return assertion != null; }
    public Request getRequest() { return request; }
    public Assertion getAssertion() { return assertion; }

    public String toString() {
        if (isRequest()) {
            return "Request: " + request;
        } else {
            return "Assertion: " + assertion;
        }
    }
}