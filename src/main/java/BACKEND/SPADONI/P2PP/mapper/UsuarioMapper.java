package BACKEND.SPADONI.P2PP.mapper;

import BACKEND.SPADONI.P2PP.DTO.UsuarioDTO;
import BACKEND.SPADONI.P2PP.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    
    // Pasajes de MODELO a DTO
    public UsuarioDTO toDTO(Usuario u){
        
        UsuarioDTO dto = new UsuarioDTO();
        
        dto.setId(u.getId());
        dto.setNombreUsuario(u.getNombreUsuario());
        dto.setRol(u.getRol());
        dto.setActivo(u.getActivo());
        
        return dto;
    }
    
    public List<UsuarioDTO> listToDTO(List<Usuario> usuarios){
        
        List<UsuarioDTO> dtos = new ArrayList<>();
        
        for(Usuario usuario : usuarios){
            dtos.add(toDTO(usuario));
        }
        
        return dtos;
    }
    
    
    // Pasajes de DTO a MODELO

}