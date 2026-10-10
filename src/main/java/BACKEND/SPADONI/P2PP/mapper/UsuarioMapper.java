package BACKEND.SPADONI.P2PP.mapper;

import BACKEND.SPADONI.P2PP.DTO.RespuestaDatosClienteDTO;
import BACKEND.SPADONI.P2PP.DTO.UsuarioDTO;
import BACKEND.SPADONI.P2PP.DTO.UsuarioListadoDTO;
import BACKEND.SPADONI.P2PP.model.Cliente;
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
    
    public List<UsuarioDTO> toDTO(List<Usuario> usuarios){
        
        List<UsuarioDTO> dtos = new ArrayList<>();
        
        for(Usuario usuario : usuarios){
            dtos.add(toDTO(usuario));
        }
        
        return dtos;
    }
    
    
    // ----- LOS DE ABAJO SON MAPPERS PARA MOSTRAR CUALQUIER TIPO DE USUARIO EN LA LISTA DE USUARIOS
    
    public UsuarioListadoDTO toListadoDTO(Usuario u) {

        UsuarioListadoDTO dto = new UsuarioListadoDTO();

        dto.setUsuario(toDTO(u));

        if (u instanceof Cliente) {
            Cliente cliente = (Cliente) u;

            RespuestaDatosClienteDTO datos = new RespuestaDatosClienteDTO();

            datos.setNombre(cliente.getNombre());
            datos.setDireccion(cliente.getDireccion());
            datos.setEmail(cliente.getEmail());
            datos.setTelefono(cliente.getTelefono());

            dto.setDatosCliente(datos);
        }

        return dto;
    }
    
    public List<UsuarioListadoDTO> toListadoDTO(List<Usuario> usuarios){
        
        List<UsuarioListadoDTO> dtos = new ArrayList<>();
        
        for(Usuario usuario : usuarios){
            dtos.add(toListadoDTO(usuario));
        }
        
        return dtos;
    }
    
    
    
    
    // Pasajes de DTO a MODELO

}