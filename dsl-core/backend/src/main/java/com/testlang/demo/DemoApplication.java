package com.testlang.demo;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // --- Data Model Simulators (In-Memory) ---
    private final Map<Integer, Map<String, Object>> users = new HashMap<>();

    public DemoApplication() {
        // Initial user for GET/PUT tests
        Map<String, Object> user42 = new HashMap<>();
        user42.put("id", 42);
        user42.put("username", "testuser");
        user42.put("role", "USER");
        users.put(42, user42);
    }

    // 1. POST /api/login (Reference Case 1)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if ("admin".equals(username) && "1234".equals(password)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("token", "jwt_abc123");

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(response);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // 2. GET /api/users/{id} (Reference Case 2)
    @GetMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable int id) {
        Map<String, Object> user = users.get(id);
        if (user != null) {
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(user);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // 3. PUT /api/users/{id} (Reference Case 3)
    @PutMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable int id,
                                                          @RequestBody Map<String, Object> updates) {
        Map<String, Object> user = users.get(id);
        if (user != null) {
            user.putAll(updates); // Apply updates

            Map<String, Object> response = new HashMap<>();
            response.put("updated", true);
            response.put("id", id);
            response.putAll(user); // Show the final state

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .header("X-App", "TestLangDemo") // Required header for assertion
                    .body(response);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}