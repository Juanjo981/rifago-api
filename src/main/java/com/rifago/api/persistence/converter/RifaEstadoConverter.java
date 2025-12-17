package com.rifago.api.persistence.converter;


import com.rifago.api.enums.RifaEstado;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RifaEstadoConverter
        implements AttributeConverter<RifaEstado, String> {

    @Override
    public String convertToDatabaseColumn(RifaEstado attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public RifaEstado convertToEntityAttribute(String dbData) {
        return dbData != null ? RifaEstado.valueOf(dbData) : null;
    }
}
