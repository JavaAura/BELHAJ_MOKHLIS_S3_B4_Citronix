package com.app.Citronix.Model.View;

import java.time.LocalDate;

import lombok.Data;

@Data
public class FermeView {

    private String nom;
    private String adress;
    private LocalDate dateCreation;
    private Double superficie;

    
}
