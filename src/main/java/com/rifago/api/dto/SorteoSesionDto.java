package com.rifago.api.dto;
import com.rifago.api.enums.SorteoEstado;
import com.rifago.api.modelos.AdminUser;
import com.rifago.api.modelos.Rifa;
import com.rifago.api.modelos.SorteoSesion;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SorteoSesionDto {

    private Long id;
    private Long rifaId;
    private OffsetDateTime startedAt;
    private OffsetDateTime endedAt;
    private SorteoEstado estado;
    private Long createdByAdminId;

    public static SorteoSesionDto fromEntity(SorteoSesion entity) {
        return SorteoSesionDto.builder()
                .id(entity.getId())
                .rifaId(entity.getRifa().getId())
                .startedAt(entity.getStartedAt())
                .endedAt(entity.getEndedAt())
                .estado(entity.getEstado())
                .createdByAdminId(
                        entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null
                )
                .build();
    }

    public SorteoSesion toEntity() {
        SorteoSesion sesion = new SorteoSesion();
        sesion.setId(this.id);
        sesion.setStartedAt(this.startedAt);
        sesion.setEndedAt(this.endedAt);
        sesion.setEstado(this.estado);

        if (this.rifaId != null) {
            Rifa rifa = new Rifa();
            rifa.setId(this.rifaId);
            sesion.setRifa(rifa);
        }

        if (this.createdByAdminId != null) {
            AdminUser admin = new AdminUser();
            admin.setId(this.createdByAdminId);
            sesion.setCreatedBy(admin);
        }

        return sesion;
    }
}

