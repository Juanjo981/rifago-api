package com.rifago.api.dto;
import com.rifago.api.modelos.Participante;
import com.rifago.api.modelos.Premio;
import com.rifago.api.modelos.Rifa;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PremioDto {

    private Long id;
    private Long rifaId;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String fotoUrl;
    private Integer orden;
    private Long ganadorParticipanteId;

    public static PremioDto fromEntity(Premio entity) {
        return PremioDto.builder()
                .id(entity.getId())
                .rifaId(entity.getRifa().getId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .precio(entity.getPrecio())
                .fotoUrl(entity.getFotoUrl())
                .orden(entity.getOrden())
                .ganadorParticipanteId(
                        entity.getGanador() != null ? entity.getGanador().getId() : null
                )
                .build();
    }

    public Premio toEntity() {
        Premio premio = new Premio();
        premio.setId(this.id);
        premio.setNombre(this.nombre);
        premio.setDescripcion(this.descripcion);
        premio.setPrecio(this.precio);
        premio.setFotoUrl(this.fotoUrl);
        premio.setOrden(this.orden);

        if (this.rifaId != null) {
            Rifa rifa = new Rifa();
            rifa.setId(this.rifaId);
            premio.setRifa(rifa);
        }

        if (this.ganadorParticipanteId != null) {
            Participante p = new Participante();
            p.setId(this.ganadorParticipanteId);
            premio.setGanador(p);
        }

        return premio;
    }
}
