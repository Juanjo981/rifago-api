package com.rifago.api.repositorios;

import com.rifago.api.enums.SorteoEstado;
import com.rifago.api.modelos.SorteoSesion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SorteoSesionRepository extends JpaRepository<SorteoSesion, Long> {

    Optional<SorteoSesion> findByRifaIdAndEstado(Long rifaId, SorteoEstado estado);

    boolean existsByRifaIdAndEstado(Long rifaId, SorteoEstado estado);

}
