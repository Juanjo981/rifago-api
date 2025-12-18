package com.rifago.api.dto.sorteo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SorteoPremioConfigResponse {

    private Long premioId;
    private Integer totalSalidas;
    private Integer ganadorEn;
}
