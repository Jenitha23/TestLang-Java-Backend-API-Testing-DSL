package com.testlang.ast;

public class Program {
    private Config config;
    private VariableDeclarations variables;
    private Tests tests;

    public Program(Config config, VariableDeclarations variables, Tests tests) {
        this.config = config;
        this.variables = variables;
        this.tests = tests;
    }

    public Config getConfig() { return config; }
    public VariableDeclarations getVariables() { return variables; }
    public Tests getTests() { return tests; }
}