package com.app.Citronix.Model.DTO.Request;

import com.app.Citronix.Model.Enum.Saison;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;

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
    private List<DetailRecolteRequest> detailRecoltes;
} 