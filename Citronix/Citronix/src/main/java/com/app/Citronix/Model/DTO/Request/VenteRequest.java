package com.app.Citronix.Model.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenteRequest {
    private Long id;

   
    private Double prixUnitaire;


    private LocalDate dateVente;

    private String client;

    private Double quantite;

    private RecolteRequest recolte;

    // Method to calculate revenue
    public double calculateRevenu() {
        return this.prixUnitaire * this.quantite;
    }
}
