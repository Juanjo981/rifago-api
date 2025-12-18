package com.rifago.api.repositorios;

import com.rifago.api.modelos.SorteoPremioConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SorteoPremioConfigRepository
        extends JpaRepository<SorteoPremioConfig, Long> {

    Optional<SorteoPremioConfig>
    findBySorteoSesionIdAndPremioId(Long sorteoSesionId, Long premioId);

    boolean existsBySorteoSesionIdAndPremioId(Long sorteoSesionId, Long premioId);
}
