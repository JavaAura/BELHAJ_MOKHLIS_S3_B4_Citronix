package com.app.Citronix.Model.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecolteRequest {
    private int id;
    
    @NotNull(message = "La date de récolte est obligatoire")
    @PastOrPresent(message = "La date de récolte ne peut pas être dans le futur")
    private LocalDate dateRecolte;
    
    private Double totalQuantite;
} 