package com.RentIt.Rent_it_spring.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JWTUtil {

    // Extracts the username/email stored in the JWT (subject).
    public String extractUserName(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    ;

    // Generates a JWT with no extra claims.
    public String generateToken(UserDetails userDetails) {

        return generateToken(new HashMap<>(), userDetails);
    }

    ;

    // Checks if the JWT is: for the same user or not expired

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Generic helper to extract any claim from JWT.
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    // Builds a JWT with:
    //
    // Claims (optional extra data)
    //
    // Subject (username)
    //
    // Issued timestamp
    //
    // Expiration (here 24 minutes)
    //
    // Signed with HS256 algorithm
    // private String generateToken(Map<String, Object> extraClaims, UserDetails
    // userDetails) {
    //
    // return
    // Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
    // .setIssuedAt(new Date(System.currentTimeMillis()))
    // .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
    // .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    //
    // }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        // Add roles to the claims
        extraClaims.put("roles", userDetails.getAuthorities().stream()
                .map(Object::toString) // convert GrantedAuthority to String
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 24 minutes
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Similar to generateToken but expires in 7 days
    // private String generateRefreshToken(Map<String, Object> extraClaims,
    // UserDetails userDetails) {
    //
    // return
    // Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
    // .setIssuedAt(new Date(System.currentTimeMillis()))
    // .setExpiration(new Date(System.currentTimeMillis() + 604800000))
    // .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    //
    // }
    private String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();

        // Add role from UserDetails into claims
        if (userDetails.getAuthorities() != null && !userDetails.getAuthorities().isEmpty()) {
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            extraClaims.put("role", role);
        }

        // Build the refresh token with role included
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 604800000)) // 7 days
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Checks if token is expired by comparing expiration date with current time
    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {

        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();

    }

    // Converts a base64-encoded secret into a key for signing/verifying JWTs.
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode("413F4428472B4B6250655368566D597033797336763979244226452948404D6351");
        return Keys.hmacShaKeyFor(keyBytes);

    }
}
