package com.rifago.api.dto;
import com.rifago.api.modelos.Participante;
import com.rifago.api.modelos.Rifa;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipanteDto {

    private Long id;
    private Long rifaId;
    private String nombre;
    private String telefono;
    private OffsetDateTime createdAt;

    public static ParticipanteDto fromEntity(Participante entity) {
        return ParticipanteDto.builder()
                .id(entity.getId())
                .rifaId(entity.getRifa().getId())
                .nombre(entity.getNombre())
                .telefono(entity.getTelefono())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public Participante toEntity() {
        Participante p = new Participante();
        p.setId(this.id);
        p.setNombre(this.nombre);
        p.setTelefono(this.telefono);

        if (this.rifaId != null) {
            Rifa rifa = new Rifa();
            rifa.setId(this.rifaId);
            p.setRifa(rifa);
        }

        return p;
    }
}
