package com.app.Citronix.Model.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailRecolteResponse {
    private Double quantite;
    private ArbreResponseLight arbre;
    private RecolteResponseLight recolte;
} 