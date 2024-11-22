package com.app.Citronix.Model.DTO.Request;

import java.time.LocalDate;

import lombok.*;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FermeRequest {
    private Long id;
    private String nom;
    private String adress;
    private Double superficie;
    private LocalDate dateCreation;
}
