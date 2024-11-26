package com.app.Citronix.Model.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenteResponse {
    private Double prixUnitaire;
    private LocalDate dateVente;
    private String client;
    private Double quantite;
    private Double revenu;
    private RecolteResponseLight recolte;
} 