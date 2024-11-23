package com.app.Citronix.Model.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.app.Citronix.Model.DTO.Request.ArbreRequest;
import com.app.Citronix.Model.DTO.Response.ArbreResponse;
import com.app.Citronix.Model.DTO.Response.ArbreResponseLight;
import com.app.Citronix.Model.Entity.Arbre;

@Mapper(componentModel = "spring")
public interface ArbreMapper {
    
    Arbre toEntity(ArbreRequest arbreRequest);
    
    @Mapping(target = "champ", ignore = true)
    ArbreResponse toResponse(Arbre arbre);

    @Named("toResponseLight")
    ArbreResponseLight toResponseLight(Arbre arbre);
} 