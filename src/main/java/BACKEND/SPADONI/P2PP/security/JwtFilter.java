package BACKEND.SPADONI.P2PP.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // Este método se ejecuta en CADA request que llega al servidor
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Leemos el header "Authorization" de la request
        // React lo manda así: Authorization: Bearer eyJhbGci...
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            // Sacamos el prefijo "Bearer " (7 caracteres) para quedarnos solo con el token
            String token = header.substring(7);

            if (jwtUtil.validarToken(token)) {

                // Extraemos usuario y rol que están guardados dentro del token
                String usuario = jwtUtil.extraerUsuario(token);
                String rol = jwtUtil.extraerRol(token);

                // Spring Security necesita el prefijo "ROLE_" para reconocer los roles
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + rol);

                // Creamos el objeto de autenticación con usuario y su rol
                // El null del medio es la contraseña, no la necesitamos acá porque ya validamos el token
                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(usuario, null, List.of(authority));

                // Registramos al usuario como autenticado en el contexto de Spring para esta request
                // Después de esto Spring sabe quién es y qué puede hacer
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Siempre dejamos pasar la request al siguiente paso
        // Si no había token válido, Spring Security la bloqueará según la configuración de SecurityConfig
        filterChain.doFilter(request, response);
    }
}