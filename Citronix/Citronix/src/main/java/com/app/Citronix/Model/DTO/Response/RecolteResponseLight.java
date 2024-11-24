package com.app.Citronix.Model.DTO.Response;

import com.app.Citronix.Model.Enum.Saison;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecolteResponseLight {
    private LocalDate dateRecolte;
    private Saison saison;
    private Double totalQuantite;
} 