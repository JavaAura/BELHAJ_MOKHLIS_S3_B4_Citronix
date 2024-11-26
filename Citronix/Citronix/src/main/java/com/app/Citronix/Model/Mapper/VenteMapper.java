package com.app.Citronix.Model.Mapper;

import com.app.Citronix.Model.DTO.Request.VenteRequest;
import com.app.Citronix.Model.DTO.Response.VenteResponse;
import com.app.Citronix.Model.Entity.Vente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {RecolteMapper.class})
public interface VenteMapper {

    Vente toEntity(VenteRequest venteRequest);
    
    @Mapping(target = "recolte", source = "recolte")
    VenteResponse toResponse(Vente vente);
    
}
