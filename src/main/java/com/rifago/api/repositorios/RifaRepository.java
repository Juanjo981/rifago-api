package com.rifago.api.repositorios;
import com.rifago.api.enums.RifaEstado;
import com.rifago.api.modelos.Rifa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RifaRepository extends JpaRepository<Rifa, Long> {

    Optional<Rifa> findByCodigo(String codigo);

    List<Rifa> findByEstado(RifaEstado estado);

    List<Rifa> findByAdminId(Long adminId);
}
