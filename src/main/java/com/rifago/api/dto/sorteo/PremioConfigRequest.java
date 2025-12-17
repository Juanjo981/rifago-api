package com.rifago.api.dto.sorteo;

import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PremioConfigRequest {

    @Min(1)
    private int salidas;

    @Min(1)
    private int ganadorEn;
}
