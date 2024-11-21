package com.app.Citronix.Model.Mapper;

import org.mapstruct.Mapper;


import com.app.Citronix.Model.DTO.Request.FermeRequest;
import com.app.Citronix.Model.DTO.Response.FermeResponse;
import com.app.Citronix.Model.Entity.Ferme;

@Mapper(componentModel = "spring")
public interface FermeMapper {
    
    Ferme toEntity(FermeRequest fermeRequest);
    
    FermeResponse toResponse(Ferme ferme);
}
