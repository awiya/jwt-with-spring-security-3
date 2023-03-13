package com.idihia.config;

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

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Component
public class JwtService {

    private static final String SECRET_KEY = "6351665468576D5A7134743777217A25432A462D4A614E645267556B586E3272";


    /**
     * Method to extract the username from a JWT
     *
     * @param token
     * @return
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Method to generate a JWT for the provided user
     *
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Method to generate a JWT for the provided user
     *
     * @param claims
     * @param userDetails
     * @return
     */
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), HS256)
                .compact();
    }


    /**
     * Method to extract the expiration date from a JWT
     *
     * @param token
     * @return
     */

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    /**
     * Generic method to extract a claim from a JWT using the provided resolver function
     *
     * @param token
     * @param claimsResolver
     * @param <T>
     * @return
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    /**
     * Method to extract all claims from a JWT
     *
     * @param token
     * @return
     */

    private Claims extractAllClaims(String token) {
        // Parse the JWT and extract all claims
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * // Method to extract all claims from a JWT
     *
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        // Extract the expiration date from the token
        return extractExpiration(token).before(new Date());
    }

    /**
     * Method to verify is  token valid
     *
     * @param token
     * @param userDetails
     * @return
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
