package com.app.Citronix.Model.Entity;


import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;

import com.app.Citronix.Model.Enum.Saison;

import lombok.Data;

@Data
@Entity
@Table(name = "recolte")
public class Recolte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @PastOrPresent(message = "La date de recolte ne peut pas être dans le futur")
    private LocalDate dateRecolte;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Saison saison;

    @Column(nullable = false)
    private Double totalQuantite;

    @Transient
    private Double totalQuantiteRestante;

    @OneToMany(mappedBy = "recolte" ,fetch = FetchType.EAGER)
    private List<DetailRecolte> detailRecoltes;


    @OneToMany(mappedBy = "recolte" ,fetch = FetchType.EAGER)
    private List<Vente> ventes;

    @PrePersist
    @PostLoad
    private void determineSaisonAndTotalQuantiteAndTotalQuantiteRestante() {

        if (this.dateRecolte.getMonthValue() >= 3 && this.dateRecolte.getMonthValue() <= 5) {
            this.saison = Saison.PRINTEMPS;
        } else if (this.dateRecolte.getMonthValue() >= 6 && this.dateRecolte.getMonthValue() <= 8) {
            this.saison = Saison.ETE;
        } else if (this.dateRecolte.getMonthValue() >= 9 && this.dateRecolte.getMonthValue() <= 11) {
            this.saison = Saison.AUTOMNE;
        }else {
            this.saison = Saison.HIVER;
        }
        // calculate total quantite with null check
        if (this.detailRecoltes == null || this.detailRecoltes.isEmpty()) {
            this.totalQuantite = 0.0;
        } else {
            this.totalQuantite = this.detailRecoltes.stream()
                .filter(detail -> detail != null && detail.getQuantite() != null)
                .mapToDouble(DetailRecolte::getQuantite)
                .sum();
        }
        // calculate total quantite restante
        if (this.ventes == null || this.ventes.isEmpty()) {
            this.totalQuantiteRestante = this.totalQuantite;
        } else {
            this.totalQuantiteRestante = this.totalQuantite - this.ventes.stream()
                .filter(vente -> vente != null && vente.getQuantite() != 0)
                .mapToDouble(Vente::getQuantite)
                .sum();
        }       

    }


}

