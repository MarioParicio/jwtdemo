package org.iesch.ad.jwtdemo.servicio;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {
    static String secret = "Estoy harto de que me camien librerias ... y de los putos Deprecated";
    static Key hmacKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    public String creaJwt() {
        log.info("Creando JWT");
        Instant now = Instant.now();
        String jwtToken = Jwts.builder()
                .claim("name", "Pepe")
                .claim("email", "mpariciob@iesch.org")
                .setSubject("Juan Manuel")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(5L, ChronoUnit.DAYS)))
                .signWith(hmacKey)
                .compact();
        return jwtToken;
    }

    public Jws compruebaJwt(String jwt) {

        log.info("Comprobando JWT");
        log.info("JWT recibido: {}",jwt);
        return Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(jwt);

    }


    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    private <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);


    }

    private Claims extractAllClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public boolean validateToken(String jwt, UserDetails userDetailService) {
        final String username = extractUsername(jwt);
        return (username.equals(userDetailService.getUsername()) && !isTokenExpired(jwt));
    }

    private boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }
}
