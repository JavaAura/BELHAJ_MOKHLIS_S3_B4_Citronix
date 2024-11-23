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
public class ArbreResponse {
    private LocalDate datePlantation;
    private Double age;
    private ChampResponseLight champ;
} 