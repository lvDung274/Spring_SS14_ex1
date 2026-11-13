package com.example.demo_employee.security.principal;

import com.example.demo_employee.models.entities.User;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Lớp thông tin mở rộng của userdetails và thuộc về security
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MyUserDetails implements UserDetails {

    private User user; //đối tượng người dùng
    private Collection<? extends GrantedAuthority> authorities; //danh sách các quyền của người dùng


    public static MyUserDetails build(User user) {
        return MyUserDetails.builder()
                .user(user)
                //lấy trực tiếp user.getRoleName() và bọc nó vào một danh sách (List)
                .authorities(List.of(new SimpleGrantedAuthority(user.getRoleName().name())))
                .build();
    }


    @Override
    public @Nullable String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }
}
