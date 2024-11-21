package com.app.Citronix.Model.DTO.Response;

import java.time.LocalDate;

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

}