package com.app.Citronix.Model.DTO.Request;

import lombok.*;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailRecolteRequest {
    private int id;
    
    @Min(value = 0, message = "La quantité doit être supérieure à 0")
    private Double quantite;
    
    private ArbreRequest arbre;
    private RecolteRequest recolte;
} 