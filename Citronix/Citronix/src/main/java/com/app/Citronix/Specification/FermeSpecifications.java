package com.app.Citronix.Specification;

import com.app.Citronix.Model.Entity.Ferme;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;


public class FermeSpecifications {
    
     public static Specification<Ferme> withNom(String nom) {
        return (root, query, criteriaBuilder) ->
                nom == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("nom")), "%" + nom.toLowerCase() + "%");
    }

    public static Specification<Ferme> withAdress(String adress) {
        return (root, query, criteriaBuilder) ->
                adress == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("adress")), "%" + adress.toLowerCase() + "%");
    }

    public static Specification<Ferme> withMinSuperficie(Double minSuperficie) {
        return (root, query, criteriaBuilder) ->
                minSuperficie == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("superficie"), minSuperficie);
    }

    public static Specification<Ferme> withMaxSuperficie(Double maxSuperficie) {
        return (root, query, criteriaBuilder) ->
                maxSuperficie == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("superficie"), maxSuperficie);
    }

    public static Specification<Ferme> withStartDate(LocalDate startDate) {
        return (root, query, criteriaBuilder) ->
                startDate == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("dateCreation"), startDate);
    }

    public static Specification<Ferme> withEndDate(LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                endDate == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("dateCreation"), endDate);
    }
}
