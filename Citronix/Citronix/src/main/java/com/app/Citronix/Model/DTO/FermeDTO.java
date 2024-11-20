package com.app.Citronix.Model.DTO;

import lombok.Data;


import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class FermeDTO {


    private Long id;
    
    @NotNull(message = "Le nom de la ferme est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;
    
    @NotNull(message = "La localisation est obligatoire")
    @Size(min = 2, max = 200, message = "La localisation doit contenir entre 2 et 200 caractères")
    private String adress;
    
    @NotNull(message = "La superficie est obligatoire")
    @Positive(message = "La superficie doit être supérieure à 0")
    @DecimalMin(value = "0.1", message = "La superficie minimale d'un champ doit être de 0.1 hectare")
    private Double superficie;
    
    @PastOrPresent(message = "La date de création ne peut pas être dans le futur")
    private LocalDate dateCreation;

        
    
}