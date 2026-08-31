package BACKEND.SPADONI.P2PP.controllers;

import BACKEND.SPADONI.P2PP.DTO.LoginDTO;
import BACKEND.SPADONI.P2PP.DTO.TokenDTO;
import BACKEND.SPADONI.P2PP.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${app.frontend.url}")
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) { this.authService = authService; }
    
    
    @PostMapping("/ingresar")
    public ResponseEntity<TokenDTO> ingresar(@Valid @RequestBody LoginDTO credenciales) {
        
        return ResponseEntity.ok(authService.ingresar(credenciales));
    }
}