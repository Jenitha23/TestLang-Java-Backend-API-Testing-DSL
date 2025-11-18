package com.testlang;

import com.testlang.lexer.Lexer;
import com.testlang.ast.Program;
import com.testlang.codegen.JUnitGenerator;
import java_cup.runtime.Symbol;
import com.testlang.parser.*;

import java.io.*;

public class Driver {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java com.testlang.Driver <input.test> <output.java>");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = args[1];

        try {
            // Create lexer and parser
            Lexer lexer = new Lexer(new FileReader(inputFile));
            Parser parser = new Parser(lexer);

            // Parse the input
            Symbol result = parser.parse();
            Program program = (Program) result.value;

            // Generate JUnit code
            JUnitGenerator generator = new JUnitGenerator(program);
            String javaCode = generator.generate();

            // Write output
            try (PrintWriter out = new PrintWriter(outputFile)) {
                out.println(javaCode);
            }

            System.out.println("Successfully generated: " + outputFile);

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + inputFile);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error during parsing/generation: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}