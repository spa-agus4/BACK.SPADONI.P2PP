package BACKEND.SPADONI.P2PP.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String SECRET;  // Clave secreta para firmar el token. Debe tener al menos 32 caracteres.

    @Value("${app.jwt.expiracion}")
    private long HORAS; // Las horas están en las props de la app

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generarToken(String nombreUsuario, String rol, Long id) {
        long expiracion = 1000L * 60 * 60 * HORAS;
        return Jwts.builder()
                .subject(nombreUsuario)
                .claim("rol", rol)
                .claim("id", id)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiracion))
                .signWith(getKey())
                .compact();
    }

    public String extraerUsuario(String token) {
        return getClaims(token).getSubject();
    }

    public String extraerRol(String token) {
        return getClaims(token).get("rol", String.class);
    }

    public boolean validarToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Para parsear y verificar el token
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    public Long extraerId(String token) {
        return getClaims(token).get("id", Long.class);
    }
}