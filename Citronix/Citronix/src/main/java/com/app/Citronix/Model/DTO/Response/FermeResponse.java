package com.app.Citronix.Model.DTO.Response;

import java.time.LocalDate;
import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FermeResponse {
    private String nom;
    private String adress;
    private Double superficie;
    private LocalDate dateCreation;
    private List<ChampResponseLight> champs;

}
