package BACKEND.SPADONI.P2PP.controllers;

import BACKEND.SPADONI.P2PP.DTO.LoginDTO;
import BACKEND.SPADONI.P2PP.DTO.TokenDTO;
import BACKEND.SPADONI.P2PP.model.Usuario;
import BACKEND.SPADONI.P2PP.security.JwtUtil;
import BACKEND.SPADONI.P2PP.security.SecurityConfig;
import BACKEND.SPADONI.P2PP.services.UsuarioService;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${app.frontend.url}")  // React
public class AuthController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @PostMapping("/ingresar")
    public ResponseEntity<?> ingresar(@RequestBody LoginDTO credenciales) {
        Optional<Usuario> usuarioOpt = service.buscarParaLogin(credenciales.getNombreUsuario());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            boolean passwordOk = usuario.getContrasena() != null
                    && passwordEncoder.matches(credenciales.getContrasena(), usuario.getContrasena());

            if (passwordOk && Boolean.TRUE.equals(usuario.getActivo())) {   // evitamos NullPointerException
                return ResponseEntity.ok(new TokenDTO(jwtUtil.generarToken(
                    usuario.getNombreUsuario(),
                    usuario.getRol().name(),
                    usuario.getId()
                )));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Usuario o contraseña incorrectos"));
    }
}
