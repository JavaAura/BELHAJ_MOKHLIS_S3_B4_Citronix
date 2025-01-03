package com.app.Citronix.Model.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.app.Citronix.Model.DTO.Request.ChampRequest;
import com.app.Citronix.Model.DTO.Response.ChampResponse;
import com.app.Citronix.Model.DTO.Response.ChampResponseLight;
import com.app.Citronix.Model.Entity.Champ;

@Mapper(componentModel = "spring", uses = {FermeMapper.class,ArbreMapper.class})
public interface ChampMapper {
    
    Champ toEntity(ChampRequest champRequest);
    
    @Mapping(target = "ferme", source = "ferme", qualifiedByName = "toResponseLightFerme")
    @Mapping(target="arbres",source="arbres")
    ChampResponse toResponse(Champ champ);

    @Named("toResponseLightChamp")
    ChampResponseLight toResponseLight(Champ champ);
} 