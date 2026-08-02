package BACKEND.SPADONI.P2PP.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

// Le dice a Spring que esta es una clase de configuración
@Configuration
// Activa Spring Security en el proyecto
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
            var config = new org.springframework.web.cors.CorsConfiguration();
            config.setAllowedOrigins(List.of(frontendUrl));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);
            return config;
        }))
                // Desactivamos CSRF porque usamos JWT, no cookies de sesión
                // CSRF protege contra ataques en apps con sesiones, no aplica acá
                .csrf(csrf -> csrf.disable())
                // Le decimos a Spring que no guarde sesiones
                // Cada request se autentica con su propio token, no hay sesión en el servidor
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configuramos qué endpoints son públicos y cuáles requieren autenticación
                .authorizeHttpRequests(auth -> auth
                // 1. Endpoints Públicos
                .requestMatchers("/api/auth/**").permitAll() // El endpoint de login o register es público, cualquiera puede acceder sin token
                .requestMatchers("/error").permitAll() // PERMITIR EL DISPATCH DE ERRORES (Esto suele arreglar el 403 fantasma)
                        
                // 2. REGLA ESPECIAL PARA USUARIOS: 
                // Permitir el listado de usuarios a GERENTE y ADMINISTRADOR (el cliente rebotará por rol o por el método polimórfico)
                .requestMatchers(HttpMethod.GET, "/api/usuarios").hasAnyRole("GERENTE", "ADMINISTRADOR")
                // Editar / Crear / Eliminar usuarios en general (solo ADMIN)
                .requestMatchers("/api/usuarios/**").hasRole("ADMINISTRADOR")
                        
                // 3. REGLAS PARA DESARROLLADORES -----------
                // El listado global de todos los desarrolladores es solo para GERENTE y ADMINISTRADOR
                .requestMatchers(HttpMethod.GET, "/api/desarrolladores").hasAnyRole("GERENTE", "ADMINISTRADOR")
                // La consulta de desarrolladores por proyecto la pueden hacer CLIENTE, GERENTE y ADMINISTRADOR
                .requestMatchers(HttpMethod.GET, "/api/desarrolladores/proyecto/**").hasAnyRole("CLIENTE", "GERENTE", "ADMINISTRADOR")
                // La asignación de desarrolladores la puede hacer el GERENTE y ADMINISTRADOR
                .requestMatchers(HttpMethod.POST, "/api/desarrolladores/proyecto/*/asignar").hasAnyRole("GERENTE", "ADMINISTRADOR")
                // Crear, editar o eliminar desarrolladores individuales queda reservado EXCLUSIVAMENTE para el ADMINISTRADOR
                .requestMatchers("/api/desarrolladores/**").hasRole("ADMINISTRADOR")
                // ------------------------------------------
                        
                // 4. REGLAS PARA PROYECTOS
                // Permitir GET (listar y obtener por ID) a CLIENTE y GERENTE
                .requestMatchers(HttpMethod.GET, "/api/proyectos/**").hasAnyRole("CLIENTE", "GERENTE")
                // Cualquier otro método HTTP (POST, PUT, DELETE) sobre proyectos, solo para GERENTE
                .requestMatchers("/api/proyectos/**").hasRole("GERENTE")
                
                // 5. REGLAS PARA CLIENTES
                // Listado y consulta de clientes: GERENTE y ADMINISTRADOR
                .requestMatchers(HttpMethod.GET, "/api/clientes/**").hasAnyRole("GERENTE", "ADMINISTRADOR")
                // Crear, editar o eliminar clientes (datos personales): solo GERENTE
                .requestMatchers("/api/clientes/**").hasRole("GERENTE")
                        
                // Cualquier otra request requiere estar autenticado
                .anyRequest().authenticated()
                )
                // Registramos nuestro JwtFilter para que corra ANTES del filtro de autenticación
                // de Spring Security, así Spring ya sabe quién es el usuario cuando llega
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}