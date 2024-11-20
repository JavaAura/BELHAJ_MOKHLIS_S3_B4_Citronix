package com.app.Citronix.Model.Mapper;

import org.mapstruct.Mapper;
import com.app.Citronix.Model.DTO.FermeDTO;
import com.app.Citronix.Model.Entity.Ferme;
import com.app.Citronix.Model.View.FermeView;
import java.util.List;


@Mapper(componentModel = "spring")
public interface FermeMapper {
    FermeDTO toDto(Ferme ferme);

    Ferme toEntity(FermeDTO fermeDTO);
    FermeView toView(Ferme ferme);
    List<FermeDTO> toDtoList(List<Ferme> fermes);
    List<Ferme> toEntityList(List<FermeDTO> fermeDTOs);
}
