package com.app.Citronix.Model.Entity;

import javax.persistence.*;
import javax.validation.constraints.Min;

import lombok.Data;

@Data
@Entity
@Table(name = "detail_recolte")
public class DetailRecolte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Min(value = 0, message = "La quantité doit être supérieure à 0")
    @Column(nullable = false)
    private Double quantite;

    @ManyToOne
    @JoinColumn(name = "arbre_id", insertable = false, updatable = false)
    private Arbre arbre;

    @ManyToOne
    @JoinColumn(name = "recolte_id", insertable = false, updatable = false)
    private Recolte recolte;
}
