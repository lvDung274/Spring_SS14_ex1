package com.example.demo_employee.controllers;

import com.example.demo_employee.models.dto.req.LoginRequest;
import com.example.demo_employee.models.dto.req.RegisterRequest;
import com.example.demo_employee.models.entities.Employee;
import com.example.demo_employee.models.repositories.EmployeeRepository;
import com.example.demo_employee.models.services.AuthService;
import com.example.demo_employee.models.services.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.status(201).body("Register Successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

}
