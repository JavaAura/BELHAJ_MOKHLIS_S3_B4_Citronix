package com.app.Citronix.Model.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "champs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Champ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Le nom du champ est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @NotNull(message = "La superficie est obligatoire")
    @DecimalMin(value = "0.1", message = "La superficie minimale d'un champ doit être de 0.1 hectare")
    private Double superficie;

    @ManyToOne
    @JoinColumn(name = "ferme_id")
    private Ferme ferme;
}
