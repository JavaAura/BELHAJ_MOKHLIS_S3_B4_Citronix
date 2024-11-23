package com.app.Citronix.Controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.Citronix.Exception.ChampException;
import com.app.Citronix.Model.DTO.Request.ChampRequest;
import com.app.Citronix.Model.DTO.Response.ChampResponse;
import com.app.Citronix.Service.ChampService;

@RestController
@RequestMapping("/api/champs")
public class ChampController {
    
    @Autowired
    private ChampService champService;
    @PostMapping
    public ResponseEntity<ChampResponse> createChamp(@Valid @RequestBody ChampRequest request) {
        return ResponseEntity.ok(champService.saveChamp(request));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ChampResponse> getChampById(@PathVariable Integer id) {
        ChampResponse champ = champService.getChampById(id);
        if (champ == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(champ);
    }

    @GetMapping
    public ResponseEntity<Page<ChampResponse>> getAllChamps(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(champService.getAllChamps(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChampResponse> updateChamp(
            @PathVariable Integer id,
            @Valid @RequestBody ChampRequest request) {
        ChampResponse updatedChamp = champService.updateChamp(id, request);
        if (updatedChamp == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedChamp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChamp(@PathVariable Integer id) {
        if (champService.deleteChamp(id)) {
            return ResponseEntity.noContent().build();
        }
        throw new ChampException("Champ non trouvé avec l'id: " + id);
        }
} 