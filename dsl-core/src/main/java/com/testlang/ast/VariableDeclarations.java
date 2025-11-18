package com.testlang.ast;

import java.util.*;

public class VariableDeclarations {
    private List<VariableDeclaration> declarations = new ArrayList<>();

    public void addDeclaration(VariableDeclaration decl) {
        declarations.add(decl);
    }

    public List<VariableDeclaration> getDeclarations() { return declarations; }

    public Map<String, Object> getVariablesMap() {
        Map<String, Object> vars = new HashMap<>();
        for (VariableDeclaration decl : declarations) {
            vars.put(decl.getName(), decl.getValue());
        }
        return vars;
    }
}