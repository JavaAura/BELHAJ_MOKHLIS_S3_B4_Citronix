package com.app.Citronix.Model.DTO.Response;

import java.time.LocalDate;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenteResponseLight {
    private Double prixUnitaire;
    private LocalDate dateVente;
    private String client;
    private Double quantite;
    private Double revenu;
}
