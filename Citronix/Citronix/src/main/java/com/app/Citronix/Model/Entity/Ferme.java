package com.app.Citronix.Model.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "fermes")
public class Ferme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull(message = "Le nom de la ferme est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @Column(nullable = false)
    @NotNull(message = "La localisation est obligatoire")
    @Size(min = 2, max = 200, message = "La localisation doit contenir entre 2 et 200 caractères")
    private String adress;

    @Column(nullable = false)
    @NotNull(message = "La superficie est obligatoire")
    @Positive(message = "La superficie doit être supérieure à 0")
    @DecimalMin(value = "0.1", message = "La superficie minimale d'un champ doit être de 0.1 hectare")
    private Double superficie;
    @Column(name = "date_creation")
    @PastOrPresent(message = "La date de création ne peut pas être dans le futur")
    private LocalDate dateCreation;

    @OneToMany(mappedBy = "ferme")
    private List<Champ> champs;
} 