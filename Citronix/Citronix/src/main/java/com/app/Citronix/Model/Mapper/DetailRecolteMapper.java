package com.app.Citronix.Model.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.app.Citronix.Model.DTO.Request.DetailRecolteRequest;
import com.app.Citronix.Model.DTO.Response.DetailRecolteResponse;
import com.app.Citronix.Model.DTO.Response.DetailRecolteResponseLight;
import com.app.Citronix.Model.Entity.DetailRecolte;

@Mapper(componentModel = "spring", uses = {ArbreMapper.class})
public interface DetailRecolteMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "arbre", source = "arbre")
    @Mapping(target = "recolte", source = "recolte")
    DetailRecolte toEntity(DetailRecolteRequest detailRecolteRequest);
    
    @Mapping(target = "arbre", source = "arbre" )
    @Mapping(target = "recolte", source = "recolte" )
    DetailRecolteResponse toResponse(DetailRecolte detailRecolte);
    
 
    @Named("toResponseLight")
    DetailRecolteResponseLight toResponseLight(DetailRecolte detailRecolte);

} 