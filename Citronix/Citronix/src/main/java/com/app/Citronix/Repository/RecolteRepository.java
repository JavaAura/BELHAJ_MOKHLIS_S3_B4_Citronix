package com.app.Citronix.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.Citronix.Model.Entity.Recolte;
import com.app.Citronix.Model.Enum.Saison;

public interface RecolteRepository extends JpaRepository<Recolte, Long> {

   @Query("SELECT r FROM Recolte r WHERE r.saison = :saison AND EXTRACT(YEAR FROM r.dateRecolte) = :dateYear")
   Optional<Recolte> findBySaisonAndDateYear(@Param("saison") Saison saison, @Param("dateYear") int dateYear);
}
