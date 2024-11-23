package com.app.Citronix.Model.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    @ManyToOne
    @JoinColumn(name = "champ_id")
    @NotNull(message = "Le champ est obligatoire")
    private Champ champ;
} 