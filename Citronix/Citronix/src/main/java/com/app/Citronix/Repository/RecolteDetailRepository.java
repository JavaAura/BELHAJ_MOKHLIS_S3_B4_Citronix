package com.app.Citronix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Citronix.Model.Entity.DetailRecolte;

public interface RecolteDetailRepository extends JpaRepository<DetailRecolte, Integer> {

    DetailRecolte findByRecolteIdAndArbreId(long recolteId, long arbreId);
    
}
