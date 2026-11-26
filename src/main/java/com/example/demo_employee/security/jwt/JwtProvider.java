package com.example.demo_employee.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtProvider {
    @Value("spring.jwt.secret")
    private String secret;


    @Value("spring.jwt.expried")
    private Long expired;


    public Key getSignKey() {
        byte[] bytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(bytes);
    }
    public String crateToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)  // Thành phần body
                .setSubject(username) // Thành phần body
                .setIssuedAt(new Date(System.currentTimeMillis()))        //Thời gian bắt đầu tại token
                .setExpiration(new Date(System.currentTimeMillis() + expired))   // Thời gian hết hạn của token
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        // Nhưng thông số cần gửi kèm theo nữa thì add vào claims ở đây
        return crateToken(claims, username);
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();  //Thành phân fpayload lấy từ Token
    }
    // 2.2 tạo ra hàm lấy từng thành phần trong claims (hàm sử dụng chung)
    public <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }
    // 2.3 tạo ra hàm lấy username
    public String extractUsername(String token) {
        return extractClaims(token, claims -> claims.get("username", String.class));
    }

    // 2.4 tạo ra hàm lấy thời gian sống của token đó (mục đích kiểm tra token còn sử dụng được hay không)
    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }


    //3.Kiểm tra xem Token có phải của hệ thống mình hay không

    public boolean isTokenExpired(String token) { //kiểm tra thời gian
        // thời gian hết hạn mà nằm trước hiện tại
        return extractExpiration(token).before(new Date());
    }

    public boolean isValidationToken(String token, UserDetails userDetails) {      //kiểm tra có phải Token của hệ thống không
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

}
