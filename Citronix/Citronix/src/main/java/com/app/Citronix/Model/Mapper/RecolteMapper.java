package com.app.Citronix.Model.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.app.Citronix.Model.DTO.Request.RecolteRequest;
import com.app.Citronix.Model.DTO.Response.RecolteResponse;
import com.app.Citronix.Model.DTO.Response.RecolteResponseLight;
import com.app.Citronix.Model.Entity.Recolte;

@Mapper(componentModel = "spring", uses = {DetailRecolteMapper.class})
public interface RecolteMapper {
    
    Recolte toEntity(RecolteRequest recolteRequest);
    
    @Mapping(target = "detailRecoltes", source = "detailRecoltes", qualifiedByName = "toResponseLight")
    @Mapping(target = "ventes", source = "ventes")
    RecolteResponse toResponse(Recolte recolte);


    @Named("toResponseLightRecolte")
    RecolteResponseLight toResponseLight(Recolte recolte);
} 