package com.app.Citronix.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Citronix.Model.Entity.Recolte;
import com.app.Citronix.Model.Enum.Saison;

public interface RecolteRepository extends JpaRepository<Recolte, Integer> {
   Recolte findBySaisonAndDateYear(Saison saison, int year);
}
