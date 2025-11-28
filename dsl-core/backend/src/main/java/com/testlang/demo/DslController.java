package com.testlang.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

// This matches the '/api' prefix from your vite.config.js
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class DslController {

    // Simple class to map the incoming JSON request body { "source": "..." }
    public static class SourceCodeRequest {
        public String source;
    }

    // --- 1. THE VALIDATE API ---
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateDsl(@RequestBody SourceCodeRequest request) {
        // NOTE: You must replace the comments below with actual calls
        // to your com.testlang.Driver logic for parsing/validation.
        Map<String, Object> response = new HashMap<>();

        try {
            // CALL YOUR DSL PARSER HERE (e.g., Driver.parse(request.source))
            // If the parser throws an exception, it's a syntax error.

            // --- Dummy Success Response ---
            response.put("success", true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // --- Dummy Error Response ---
            response.put("success", false);
            // In a real parser, you would extract line/column details from the exception
            response.put("errors", List.of(Map.of(
                    "line", 0,
                    "column", 0,
                    "message", "Syntax error during parsing. " + e.getMessage()
            )));
            return ResponseEntity.badRequest().body(response);
        }
    }

    // --- 2. THE RUN TESTS API ---
    @PostMapping("/run")
    public ResponseEntity<Map<String, Object>> runTests(@RequestBody SourceCodeRequest request) {
        // NOTE: You must replace the comments below with actual calls
        // to your com.testlang.Driver logic for full execution.
        Map<String, Object> response = new HashMap<>();

        try {
            // CALL YOUR FULL DSL EXECUTION PROCESS HERE
            // (parse, generate, compile, run JUnit)

            // --- Dummy Test Results (Replace with actual results map) ---
            Map<String, Object> testResult = new HashMap<>();
            testResult.put("summary", "2 tests executed, 1 passed, 1 failed.");
            testResult.put("tests", List.of(
                    Map.of("name", "Login", "status", "PASSED", "durationMs", 125),
                    Map.of("name", "GetUser", "status", "FAILED", "durationMs", 90, "errorMessage", "Expected 200 but got 404.")
            ));

            return ResponseEntity.ok(testResult);

        } catch (Exception e) {
            response.put("summary", "Internal server error during test execution: " + e.getMessage());
            response.put("tests", List.of());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}