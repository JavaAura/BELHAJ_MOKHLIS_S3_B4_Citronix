package com.app.Citronix.Model.Mapper;

import com.app.Citronix.Model.DTO.FermeDTO;
import com.app.Citronix.Model.Entity.Ferme;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FermeMapper {
    
    FermeMapper INSTANCE = Mappers.getMapper(FermeMapper.class);
    
    FermeDTO toDto(Ferme ferme);
    
    Ferme toEntity(FermeDTO fermeDTO);
    
    List<FermeDTO> toDtoList(List<Ferme> fermes);
    
    List<Ferme> toEntityList(List<FermeDTO> fermeDTOs);
} 