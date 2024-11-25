package com.app.Citronix.Model.DTO.Request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChampRequest {

    private long id;
    private String nom;
    private Double superficie;
    private FermeRequest ferme;
    
}
