package BACKEND.SPADONI.P2PP.mapper;

import BACKEND.SPADONI.P2PP.DTO.ProyectoDTO;
import BACKEND.SPADONI.P2PP.model.Proyecto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProyectoMapper {

    private final ClienteMapper cMapper;

    public ProyectoMapper(ClienteMapper cMapper) {
        this.cMapper = cMapper;
    }

    // Pasajes de MODELO a DTO
    public ProyectoDTO toDTO(Proyecto p) {

        ProyectoDTO dto = new ProyectoDTO();

        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setCliente(cMapper.toDTO(p.getCliente())); // tengo que usar el Mapper de cliente para mapearlo
        dto.setCanalSolicitud(p.getCanalSolicitud());
        dto.setFechaInicio(p.getFechaInicio());
        dto.setFechaFinalizacion(p.getFechaFinalizacion());
        dto.setPresupuesto(p.getPresupuesto());
        dto.setEstadoProyecto(p.getEstadoProyecto());
        dto.setEstadoProceso(p.getEstadoProceso());

        return dto;
    }

    public List<ProyectoDTO> toDTO(List<Proyecto> proyectos) {

        List<ProyectoDTO> dtos = new ArrayList<>();

        for (Proyecto p : proyectos) {
            dtos.add(toDTO(p));
        }

        return dtos;
    }
}
