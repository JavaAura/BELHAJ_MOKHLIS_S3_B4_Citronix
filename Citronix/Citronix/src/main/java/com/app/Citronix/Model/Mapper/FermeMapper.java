package com.app.Citronix.Model.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.app.Citronix.Model.DTO.Request.FermeRequest;
import com.app.Citronix.Model.DTO.Response.FermeResponse;
import com.app.Citronix.Model.DTO.Response.FermeResponseLight;
import com.app.Citronix.Model.Entity.Ferme;

@Mapper(componentModel = "spring", uses = {ChampMapper.class})
public interface FermeMapper {
    
    Ferme toEntity(FermeRequest fermeRequest);
    
    @Mapping(target = "champs", source = "champs")
    FermeResponse toResponse(Ferme ferme);

    @Named("toResponseLight")
    FermeResponseLight toResponseLight(Ferme ferme);
}
