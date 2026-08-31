package BACKEND.SPADONI.P2PP.security;

import BACKEND.SPADONI.P2PP.model.Usuario;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    
    private final JwtUtil jwtUtil;
    
    public TokenService(JwtUtil jwtUtil){ this.jwtUtil = jwtUtil; }
    
    public String generarToken(Usuario u){
        return jwtUtil.generarToken(
                    u.getNombreUsuario(),
                    u.getRol().toString(),
                    u.getId()
            );
    }
}