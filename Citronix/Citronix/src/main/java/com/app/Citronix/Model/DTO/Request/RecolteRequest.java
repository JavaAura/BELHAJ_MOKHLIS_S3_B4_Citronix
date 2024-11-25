package com.app.Citronix.Model.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.app.Citronix.Model.Enum.Saison;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecolteRequest {
    
    private Long id;
    
    @NotNull(message = "La date de récolte est obligatoire")
    @PastOrPresent(message = "La date de récolte ne peut pas être dans le futur")
    private LocalDate dateRecolte;

    private Saison saison;

    @Min(value = 0, message = "La quantité totale doit être supérieure à 0")
    private Double totalQuantite;

    @PrePersist
    @PostLoad
    private void determineSaison() {

        if (this.dateRecolte.getMonthValue() >= 3 && this.dateRecolte.getMonthValue() <= 5) {
            this.saison = Saison.PRINTEMPS;
        } else if (this.dateRecolte.getMonthValue() >= 6 && this.dateRecolte.getMonthValue() <= 8) {
            this.saison = Saison.ETE;
        } else if (this.dateRecolte.getMonthValue() >= 9 && this.dateRecolte.getMonthValue() <= 11) {
            this.saison = Saison.AUTOMNE;
        }else {
            this.saison = Saison.HIVER;
        }
    }

   
} 