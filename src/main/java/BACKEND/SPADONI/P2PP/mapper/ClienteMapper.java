package BACKEND.SPADONI.P2PP.mapper;

import BACKEND.SPADONI.P2PP.DTO.ClienteDTO;
import BACKEND.SPADONI.P2PP.model.Cliente;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;



@Component
public class ClienteMapper {
    
    // Pasajes de MODELO a DTO
    public ClienteDTO toDTO(Cliente c){
        
        ClienteDTO dto = new ClienteDTO();
        
        dto.setId(c.getId());
        dto.setNombreUsuario(c.getNombreUsuario());
        dto.setRol(c.getRol());
        dto.setActivo(c.getActivo());
        
        dto.setNombre(c.getNombre());
        dto.setDireccion(c.getDireccion());
        dto.setEmail(c.getEmail());
        dto.setTelefono(c.getTelefono());
        
        return dto;
    }
    
    public List<ClienteDTO> listToDTO(List<Cliente> clientes){
        
        List<ClienteDTO> dtos = new ArrayList<>();
        
        for(Cliente cliente : clientes){
            dtos.add(toDTO(cliente));
        }
        
        return dtos;
    }
}