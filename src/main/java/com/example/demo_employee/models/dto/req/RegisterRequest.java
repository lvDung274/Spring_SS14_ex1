package com.example.demo_employee.models.dto.req;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "Tên người dùng không được để trống")
        String fullName,

        @NotBlank(message = "Tên đăng nhập không được để trống")
        String userName,

        @NotBlank(message = "Mật khẩu không được bỏ trống")
        String passWord
) {
}
