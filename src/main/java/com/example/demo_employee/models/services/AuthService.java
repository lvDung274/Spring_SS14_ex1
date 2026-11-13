package com.example.demo_employee.models.services;

import com.example.demo_employee.models.dto.req.LoginRequest;
import com.example.demo_employee.models.dto.req.RegisterRequest;
import com.example.demo_employee.models.dto.response.AuthResponse;
import com.example.demo_employee.models.entities.User;

public interface AuthService {
    void register(RegisterRequest req);


    AuthResponse login(LoginRequest req);
}
