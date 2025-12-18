package com.rifago.api.dto.sorteo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfigurarPremioRequest {

    @NotNull
    @Min(1)
    private Integer totalSalidas;

    @NotNull
    @Min(1)
    private Integer ganadorEn;
}
