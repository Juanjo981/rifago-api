package com.rifago.api.repositorios;
import com.rifago.api.modelos.Participante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {

    boolean existsByRifaIdAndTelefono(Long rifaId, String telefono);

    List<Participante> findByRifaId(Long rifaId);

    Optional<Participante> findByRifaIdAndTelefono(Long rifaId, String telefono);

    @Query(value = """
    SELECT p.*
    FROM rifago.participante p
    WHERE p.rifa_id = :rifaId
      AND p.id NOT IN (
          SELECT pr.ganador_participante_id
          FROM rifago.premio pr
          WHERE pr.rifa_id = :rifaId
            AND pr.ganador_participante_id IS NOT NULL
      )
      AND p.id NOT IN (
          SELECT se.participante_id
          FROM rifago.sorteo_evento se
          WHERE se.sorteo_sesion_id = :sesionId
            AND se.premio_id = :premioId
      )
    ORDER BY RANDOM()
    LIMIT 1
""", nativeQuery = true)
    Optional<Participante> findRandomElegible(
            @Param("rifaId") Long rifaId,
            @Param("sesionId") Long sesionId,
            @Param("premioId") Long premioId
    );

}
