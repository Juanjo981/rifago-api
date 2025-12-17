package com.rifago.api.dto;

import com.rifago.api.modelos.Participante;
import com.rifago.api.modelos.Premio;
import com.rifago.api.modelos.SorteoEvento;
import com.rifago.api.modelos.SorteoSesion;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SorteoEventoDto {

    private Long id;
    private Long sorteoSesionId;
    private Long premioId;
    private Long participanteId;
    private Integer indiceSalida;
    private Boolean esGanador;
    private OffsetDateTime createdAt;

    public static SorteoEventoDto fromEntity(SorteoEvento entity) {
        return SorteoEventoDto.builder()
                .id(entity.getId())
                .sorteoSesionId(entity.getSorteoSesion().getId())
                .premioId(entity.getPremio().getId())
                .participanteId(entity.getParticipante().getId())
                .indiceSalida(entity.getIndiceSalida())
                .esGanador(entity.getEsGanador())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public SorteoEvento toEntity() {
        SorteoEvento e = new SorteoEvento();
        e.setId(this.id);
        e.setIndiceSalida(this.indiceSalida);
        e.setEsGanador(this.esGanador);

        if (this.sorteoSesionId != null) {
            SorteoSesion s = new SorteoSesion();
            s.setId(this.sorteoSesionId);
            e.setSorteoSesion(s);
        }

        if (this.premioId != null) {
            Premio p = new Premio();
            p.setId(this.premioId);
            e.setPremio(p);
        }

        if (this.participanteId != null) {
            Participante pa = new Participante();
            pa.setId(this.participanteId);
            e.setParticipante(pa);
        }

        return e;
    }
}
