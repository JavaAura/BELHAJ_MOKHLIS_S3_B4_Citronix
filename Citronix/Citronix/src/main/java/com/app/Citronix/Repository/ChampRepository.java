package com.app.Citronix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Citronix.Model.Entity.Champ;

@Repository
public interface ChampRepository extends JpaRepository<Champ, Long> {
} 