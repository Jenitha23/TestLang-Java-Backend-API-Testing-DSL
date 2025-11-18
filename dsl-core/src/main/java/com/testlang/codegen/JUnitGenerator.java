package com.testlang.codegen;

import com.testlang.ast.*;
import java.util.*;

public class JUnitGenerator {
    private Program program;
    private Map<String, Object> variables;
    private int requestCounter = 0;

    public JUnitGenerator(Program program) {
        this.program = program;
        this.variables = new HashMap<>();
        // Extract variables from variable declarations
        if (program.getVariables() != null) {
            for (VariableDeclaration var : program.getVariables().getDeclarations()) {
                variables.put(var.getName(), var.getValue());
            }
        }
    }

    public String generate() {
        StringBuilder code = new StringBuilder();

        generateImports(code);
        generateClassHeader(code);
        generateStaticFields(code);
        generateBeforeAll(code);
        generateTestMethods(code);

        code.append("}\n");
        return code.toString();
    }

    private void generateImports(StringBuilder code) {
        code.append("import org.junit.jupiter.api.*;\n");
        code.append("import static org.junit.jupiter.api.Assertions.*;\n");
        code.append("import java.net.http.*;\n");
        code.append("import java.net.*;\n");
        code.append("import java.time.Duration;\n");
        code.append("import java.nio.charset.StandardCharsets;\n");
        code.append("import java.util.*;\n\n");
    }

    private void generateClassHeader(StringBuilder code) {
        code.append("public class GeneratedTests {\n");
    }

    private void generateStaticFields(StringBuilder code) {
        Config config = program.getConfig();
        String baseUrl = "http://localhost:8080"; // default

        if (config != null && config.getBaseUrl() != null) {
            baseUrl = config.getBaseUrl();
        }

        code.append("  static String BASE = \"")
                .append(escapeJavaString(baseUrl))
                .append("\";\n");

        code.append("  static Map<String,String> DEFAULT_HEADERS = new HashMap<>();\n");
        code.append("  static HttpClient client;\n\n");
    }

    private void generateBeforeAll(StringBuilder code) {
        code.append("  @BeforeAll\n");
        code.append("  static void setup() {\n");
        code.append("    client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();\n");

        Config config = program.getConfig();
        if (config != null) {
            for (ConfigEntry entry : config.getEntries()) {
                if (entry.getType().equals("header")) {
                    code.append("    DEFAULT_HEADERS.put(\"")
                            .append(escapeJavaString(entry.getKey()))
                            .append("\", \"")
                            .append(escapeJavaString(entry.getValue()))
                            .append("\");\n");
                }
            }
        }

        code.append("  }\n\n");
    }

    private void generateTestMethods(StringBuilder code) {
        for (TestBlock test : program.getTests().getTests()) {
            generateTestMethod(code, test);
        }
    }

    private void generateTestMethod(StringBuilder code, TestBlock test) {
        code.append("  @Test\n");
        code.append("  void test_").append(test.getName()).append("() throws Exception {\n");

        // Reset request counter for each test
        requestCounter = 0;
        String responseVar = "resp";

        for (TestStep step : test.getSteps().getSteps()) {
            if (step.isRequest()) {
                generateRequest(code, step.getRequest(), responseVar);
            } else if (step.isAssertion()) {
                generateAssertion(code, step.getAssertion(), responseVar);
            }
        }

        code.append("  }\n\n");
    }

    private void generateRequest(StringBuilder code, Request request, String responseVar) {
        String method = request.getMethod();
        String path = expandVariables(request.getPath());
        RequestBlock block = request.getBlock();

        requestCounter++;
        String builderVar = "b" + requestCounter;

        // Build URI
        code.append("    // ").append(method).append(" request\n");
        code.append("    HttpRequest.Builder ").append(builderVar).append(" = HttpRequest.newBuilder(URI.create(");
        if (path.startsWith("/")) {
            code.append("BASE + \"");
            code.append(escapeJavaString(path));
            code.append("\"");
        } else {
            code.append("\"");
            code.append(escapeJavaString(path));
            code.append("\"");
        }
        code.append("))\n");
        code.append("      .timeout(Duration.ofSeconds(10));\n");

        // Set HTTP method and body
        switch (method) {
            case "GET":
                code.append("    ").append(builderVar).append(".GET();\n");
                break;
            case "DELETE":
                code.append("    ").append(builderVar).append(".DELETE();\n");
                break;
            case "POST":
            case "PUT":
                String body = "";
                if (block != null) {
                    for (RequestEntry entry : block.getEntries()) {
                        if (entry.getType().equals("body")) {
                            body = expandVariables(entry.getValue());
                            break;
                        }
                    }
                }
                code.append("    ").append(builderVar).append(".").append(method)
                        .append("(HttpRequest.BodyPublishers.ofString(\"")
                        .append(escapeJavaString(body))
                        .append("\"));\n");
                break;
        }

        // Add default headers
        code.append("    for (var e: DEFAULT_HEADERS.entrySet()) ")
                .append(builderVar).append(".header(e.getKey(), e.getValue());\n");

        // Add request-specific headers
        if (block != null) {
            for (RequestEntry entry : block.getEntries()) {
                if (entry.getType().equals("header")) {
                    code.append("    ").append(builderVar).append(".header(\"")
                            .append(escapeJavaString(entry.getKey()))
                            .append("\", \"")
                            .append(escapeJavaString(entry.getValue()))
                            .append("\");\n");
                }
            }
        }

        // Send request
        code.append("    HttpResponse<String> ").append(responseVar)
                .append(" = client.send(").append(builderVar)
                .append(".build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));\n\n");
    }

    private void generateAssertion(StringBuilder code, Assertion assertion, String responseVar) {
        switch (assertion.getType()) {
            case "status":
                code.append("    assertEquals(")
                        .append(assertion.getValue())
                        .append(", ").append(responseVar).append(".statusCode());\n");
                break;
            case "header_equals":
                code.append("    assertEquals(\"")
                        .append(escapeJavaString((String) assertion.getValue()))
                        .append("\", ").append(responseVar).append(".headers().firstValue(\"")
                        .append(escapeJavaString(assertion.getKey()))
                        .append("\").orElse(\"\"));\n");
                break;
            case "header_contains":
                code.append("    assertTrue(").append(responseVar).append(".headers().firstValue(\"")
                        .append(escapeJavaString(assertion.getKey()))
                        .append("\").orElse(\"\").contains(\"")
                        .append(escapeJavaString((String) assertion.getValue()))
                        .append("\"));\n");
                break;
            case "body_contains":
                code.append("    assertTrue(").append(responseVar).append(".body().contains(\"")
                        .append(escapeJavaString((String) assertion.getValue()))
                        .append("\"));\n");
                break;
        }
    }

    private String expandVariables(String input) {
        if (input == null) return "";

        String result = input;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String varName = "$" + entry.getKey();
            String varValue = entry.getValue().toString();
            result = result.replace(varName, varValue);
        }
        return result;
    }

    private String escapeJavaString(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}