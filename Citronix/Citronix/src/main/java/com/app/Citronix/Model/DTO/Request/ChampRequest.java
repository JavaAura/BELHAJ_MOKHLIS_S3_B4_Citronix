package com.app.Citronix.Model.DTO.Request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChampRequest {

    private int id;
    private String nom;
    private Double superficie;
    private FermeRequest ferme;
    
}
