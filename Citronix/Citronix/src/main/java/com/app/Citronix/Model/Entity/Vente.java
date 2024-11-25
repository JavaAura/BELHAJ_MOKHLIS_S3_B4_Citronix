package com.app.Citronix.Model.Entity;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "ventes")
public class Vente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Double prixUnitaire;

    @PastOrPresent(message = "La date de vente ne peut pas être dans le futur")
    private LocalDate dateVente;

    @NotBlank(message = "Le client est requis")
    private String client;

    @NotNull(message = "La quantité vendue est requise")
    private double quantiteVendu;

    @NotNull(message = "Le revenu est requis")
    private double revenu;

    @ManyToOne
    @JoinColumn(name = "recolte_id")
    private Recolte recolte;
    
}
