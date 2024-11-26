package com.app.Citronix.Model.DTO.Response;

import com.app.Citronix.Model.Enum.Saison;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecolteResponse {
    private LocalDate dateRecolte;
    private Saison saison;
    private Double totalQuantite;
    private Double totalQuantiteRestante;
    private List<DetailRecolteResponseLight> detailRecoltes;
    private List<VenteResponseLight> ventes;
} 