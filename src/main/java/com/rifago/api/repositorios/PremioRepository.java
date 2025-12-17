package com.rifago.api.repositorios;

import com.rifago.api.modelos.Premio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PremioRepository extends JpaRepository<Premio, Long> {

    List<Premio> findByRifaIdOrderByPrecioAsc(Long rifaId);

    List<Premio> findByRifaId(Long rifaId);

    boolean existsByRifaIdAndNombre(Long rifaId, String nombre);
}
