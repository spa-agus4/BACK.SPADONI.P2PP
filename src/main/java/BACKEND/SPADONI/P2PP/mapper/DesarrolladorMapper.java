package BACKEND.SPADONI.P2PP.mapper;

import BACKEND.SPADONI.P2PP.DTO.DesarrolladorDTO;
import BACKEND.SPADONI.P2PP.model.Desarrollador;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DesarrolladorMapper {
    
    private final ProyectoMapper pMapper;

    public DesarrolladorMapper(ProyectoMapper pMapper) {
        this.pMapper = pMapper;
    }
    
    // Pasajes de MODELO a DTO
    public DesarrolladorDTO toDTO(Desarrollador d) {

        DesarrolladorDTO dto = new DesarrolladorDTO();

        dto.setId(d.getId());
        dto.setNombre(d.getNombre());
        
        if(d.getProyecto() != null){ // tengo que hacer esta guarda porque si poryecto es null, me da un NullPointer Exception
            dto.setProyecto(pMapper.toDTO(d.getProyecto())); // tengo que usar el Mapper de proyecto para mapearlo
        } else {
            dto.setProyecto(null);
        }
        
        dto.setHabilidades(d.getHabilidades());
        dto.setDisponible(d.getDisponible());

        return dto;
    }

    public List<DesarrolladorDTO> listToDTO(List<Desarrollador> devs) {

        List<DesarrolladorDTO> dtos = new ArrayList<>();

        for (Desarrollador dev : devs) {
            dtos.add(toDTO(dev));
        }

        return dtos;
    }
}