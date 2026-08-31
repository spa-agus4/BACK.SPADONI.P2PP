package BACKEND.SPADONI.P2PP.services;

import BACKEND.SPADONI.P2PP.DTO.LoginDTO;
import BACKEND.SPADONI.P2PP.DTO.TokenDTO;
import BACKEND.SPADONI.P2PP.exceptions.CredencialesInvalidasException;
import BACKEND.SPADONI.P2PP.model.Usuario;
import BACKEND.SPADONI.P2PP.security.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioService usuarioService;
    private final TokenService tokenService;

    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UsuarioService usuarioService, TokenService tokenService, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenDTO ingresar(LoginDTO credenciales) {

        Usuario usuario = usuarioService.buscarParaLogin(credenciales.getNombreUsuario()); // Busca el usuario por nombre de usuario. Devuelve null si no existe

        if (usuario == null) 
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
        
        boolean passwordOk = usuario.getContrasena() != null
                && passwordEncoder.matches(credenciales.getContrasena(), usuario.getContrasena());  // Verificamos si la contraseña ingresada coincide con el nombreUsuario

        if (!passwordOk || !Boolean.TRUE.equals(usuario.getActivo())) // Nos vamos si la password está mal o el Usuario está inhabilitado
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");

        return new TokenDTO(tokenService.generarToken(usuario));
    }
}