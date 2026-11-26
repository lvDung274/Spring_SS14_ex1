package com.example.demo_employee.models.services.impl;

import com.example.demo_employee.exceptions.HttpBadRequestException;
import com.example.demo_employee.models.constants.RoleName;
import com.example.demo_employee.models.dto.req.LoginRequest;
import com.example.demo_employee.models.dto.req.RegisterRequest;
import com.example.demo_employee.models.dto.response.AuthResponse;
import com.example.demo_employee.models.entities.User;
import com.example.demo_employee.models.repositories.UserRepository;
import com.example.demo_employee.models.services.AuthService;
import com.example.demo_employee.security.jwt.JwtProvider;
import com.example.demo_employee.security.principal.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager manager;
    private final JwtProvider jwtProvider;

    @Override
    public void register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.userName())) {
            throw new UsernameNotFoundException("Username " + req.userName() + " not found");
        }


        // Khởi tạo đối tượng User mới từ request
        User user = new User();
        user.setUsername(req.userName());
        user.setPassword(passwordEncoder.encode(req.passWord()));

        user.setUsername(req.userName());
        user.setRoleName(RoleName.ROLE_USER);
        user.setEnabled(true);

        userRepository.save(user);

    }

    @Override
    public AuthResponse login(LoginRequest req) {

        Authentication authentication;
        try {
            authentication = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.userName(), req.passWord())
            );
        }catch (AuthenticationException ex){
                throw new HttpBadRequestException("User or PassWord sai " + ex.getMessage());
        }

        //Muc dich để lưu trữ
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //Lấy thông tin người dùng vừa đăng nhập
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        String token = jwtProvider.generateToken(userDetails.getUsername());

        Set<String> roles = userDetails.getUser().getRoleName() != null
                ? Set.of(userDetails.getUser().getRoleName().name())
                : Set.of();

        return new AuthResponse(
                userDetails.getUser().getId(),
                token,
                "Bearer",
                null,
                userDetails.getUsername(),
                roles
        );
    }
}
