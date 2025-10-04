package com.uteq.sgaac.mapping;

import com.uteq.sgaac.dto.RequisitoDTO;
import com.uteq.sgaac.model.Requisito;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequisitoMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "activo", target = "activo")
    @Mapping(source = "tipo", target = "tipo")
    @Mapping(source = "urlPlantilla", target = "urlPlantilla")
    RequisitoDTO toDTO(Requisito requisito);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "activo", target = "activo")
    @Mapping(source = "tipo", target = "tipo")
    @Mapping(source = "urlPlantilla", target = "urlPlantilla")
    Requisito toEntity(RequisitoDTO dto);
}
