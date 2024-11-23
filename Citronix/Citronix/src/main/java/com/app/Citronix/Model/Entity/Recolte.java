package com.app.Citronix.Model.Entity;


import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import com.app.Citronix.Model.Enum.Saison;

import lombok.Data;

@Data
@Entity
@Table(name = "recolte")
public class Recolte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private LocalDate dateRecolte;

    @Column(nullable = false)
    private Saison saison;

    @Column(nullable = false)
    private Double totalQuantite;

    @OneToMany(mappedBy = "recolte")
    private List<DetailRecolte> detailRecoltes;

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

