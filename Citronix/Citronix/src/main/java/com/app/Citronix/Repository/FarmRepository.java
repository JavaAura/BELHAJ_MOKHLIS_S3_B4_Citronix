package com.app.Citronix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Citronix.Model.Entity.Ferme;

public interface FarmRepository extends JpaRepository<Ferme, Long> {
    
}