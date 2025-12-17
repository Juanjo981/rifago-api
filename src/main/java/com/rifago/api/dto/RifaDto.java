package com.rifago.api.dto;
import com.rifago.api.enums.RifaEstado;
import com.rifago.api.modelos.AdminUser;
import com.rifago.api.modelos.Rifa;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RifaDto {

    private Long id;
    private String codigo;
    private String titulo;
    private String descripcion;
    private LocalDateTime createdAt;
    private LocalDateTime fechaRifa;
    private RifaEstado estado;
    private Long adminId;

    public static RifaDto fromEntity(Rifa entity) {
        return RifaDto.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .titulo(entity.getTitulo())
                .descripcion(entity.getDescripcion())
                .createdAt(entity.getCreatedAt())
                .fechaRifa(entity.getFechaRifa())
                .estado(entity.getEstado())
                .adminId(entity.getAdmin() != null ? entity.getAdmin().getId() : null)
                .build();
    }

    public Rifa toEntity() {
        Rifa rifa = new Rifa();
        rifa.setId(this.id);
        rifa.setCodigo(this.codigo);
        rifa.setTitulo(this.titulo);
        rifa.setDescripcion(this.descripcion);
        rifa.setFechaRifa(this.fechaRifa);
        rifa.setEstado(this.estado);

        if (this.adminId != null) {
            AdminUser admin = new AdminUser();
            admin.setId(this.adminId);
            rifa.setAdmin(admin);
        }

        return rifa;
    }
}
