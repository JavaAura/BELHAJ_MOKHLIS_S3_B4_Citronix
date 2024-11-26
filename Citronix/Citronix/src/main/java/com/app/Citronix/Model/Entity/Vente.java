package com.app.Citronix.Model.Entity;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "ventes")
public class Vente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Le prix unitaire est requis")
    @Positive(message = "Le prix unitaire doit être positif")
    private Double prixUnitaire;

    @NotBlank(message = "La date de vente est requise")
    @PastOrPresent(message = "La date de vente ne peut pas être dans le futur")
    private LocalDate dateVente;

    @NotBlank(message = "Le client est requis")
    private String client;

    @NotNull(message = "La quantité vendue est requise")
    @Positive(message = "La quantité vendue doit être positive")
    private double quantite;

    @Positive(message = "Le revenu doit être positif")
    @NotNull(message = "Le revenu est requis")
    private double revenu;

    @ManyToOne
    @JoinColumn(name = "recolte_id", nullable = false)
    private Recolte recolte;
    
    public double calculateRevenu() {
        return this.prixUnitaire * this.quantite;
    }

   
}
