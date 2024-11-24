package com.app.Citronix.Model.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.app.Citronix.Model.Enum.EtatArbre;

import java.time.LocalDate;

@Entity
@Table(name = "arbres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Arbre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "La date de plantation est obligatoire")
    @PastOrPresent(message = "La date de plantation ne peut pas être dans le futur")
    private LocalDate datePlantation;

    @Transient
    private long age;

    @Transient
    private EtatArbre etatArbre;

    @Transient
    private double production;

    @ManyToOne
    @JoinColumn(name = "champ_id")
    @NotNull(message = "Le champ est obligatoire")
    private Champ champ;

    @OneToMany(mappedBy = "arbre")
    private List<DetailRecolte> detailRecoltes;

    @PostLoad
    @PrePersist
    private void calculateAgeAndSetEtatArbreAndCalculeProduction() {
        if (this.datePlantation != null) {
            this.age = java.time.temporal.ChronoUnit.YEARS.between(
                this.datePlantation, 
                LocalDate.now()
            );
        }
       if( this.age >= 1 && this.age < 3) {
        this.etatArbre = EtatArbre.JEUNE;
        this.production = 2.5;
       } else if(this.age >= 3 && this.age <= 10) {
        this.etatArbre = EtatArbre.MATURE;
        this.production = 12;
       } else if(this.age > 10 && this.age <= 20) {
        this.etatArbre = EtatArbre.VIEUX;
        this.production = 20;
       } else {
        this.etatArbre = EtatArbre.NON_PRODUCTIF;
        this.production = 0;
       }


    }

  

  
} 