package com.example.demo_employee.models.dto.response;

import java.util.Set;

public record AuthResponse(
        Long id,
        String fullName,
        String userName

) {
}
