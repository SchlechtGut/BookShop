package com.example.MyBookShopApp.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JWTUtil {

    @Value("${auth.secret}")
    private String secret;

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public JWTUtil(@Lazy StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }


    private String createToken(Map<String, Object> claims, String username) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

///////////////////////////////////////////////////////////////////

    public void saveJwtToBlackList(String jwt) {
        stringRedisTemplate.opsForSet().add("blackList", jwt);
    }

    public boolean blackListContains(String jwt) {
        stringRedisTemplate.opsForSet().members("blackList").forEach(System.out::println);
        return stringRedisTemplate.opsForSet().members("blackList").contains(jwt);
    }

    public void deleteExpiredTokens() {
        Set<String> expiredTokens = stringRedisTemplate.opsForSet().members("blackList").stream().filter(x-> {
            try {
                this.isTokenExpired(x);
            } catch (ExpiredJwtException e) {
                return true;
            }
            return false;
        }).collect(Collectors.toSet());

        expiredTokens.forEach(x-> stringRedisTemplate.opsForSet().remove("blackList", x));
    }
}
