package com.app.Citronix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Citronix.Model.Entity.Vente;

public interface VenteRepository extends JpaRepository<Vente, Long> {
    
}
