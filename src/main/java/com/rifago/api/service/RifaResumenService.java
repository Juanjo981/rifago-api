package com.rifago.api.service;

import com.rifago.api.dto.resumen.*;

public interface RifaResumenService {

    RifaResumenPublicResponse obtenerResumenPublico(String codigoRifa);

    RifaResumenAdminResponse obtenerResumenAdmin(Long adminId, Long rifaId);
}
