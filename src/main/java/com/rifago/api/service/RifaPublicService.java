package com.rifago.api.service;

import com.rifago.api.dto.publico.*;

public interface RifaPublicService {

    ParticipantePublicResponse registrarParticipante(
            String codigoRifa,
            ParticipantePublicRequest request
    );

    RifaPublicInfoResponse obtenerInfoPublica(String codigoRifa);
}
