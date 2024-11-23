 package com.app.Citronix.Model.DTO.Request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;


import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArbreRequest {
    private Long id;
    private LocalDate datePlantation;
    private Long champId;
    private ChampRequest champ;
} 