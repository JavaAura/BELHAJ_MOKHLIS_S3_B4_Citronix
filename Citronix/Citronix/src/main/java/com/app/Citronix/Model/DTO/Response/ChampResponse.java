package com.app.Citronix.Model.DTO.Response;

import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChampResponse {
    private String nom;
    private Double superficie;
    private FermeResponseLight ferme;
    private List<ArbreResponseLight> arbres;
}
