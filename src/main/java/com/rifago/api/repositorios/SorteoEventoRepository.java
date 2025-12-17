package com.rifago.api.repositorios;

import com.rifago.api.modelos.SorteoEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SorteoEventoRepository extends JpaRepository<SorteoEvento, Long> {

    List<SorteoEvento> findBySorteoSesionIdAndPremioId(Long sorteoSesionId, Long premioId);

    boolean existsBySorteoSesionIdAndPremioIdAndParticipanteId(
            Long sorteoSesionId,
            Long premioId,
            Long participanteId
    );

    long countBySorteoSesionIdAndPremioId(Long sorteoSesionId, Long premioId);

    List<SorteoEvento> findByPremioIdOrderByIndiceSalidaAsc(Long premioId);

}
