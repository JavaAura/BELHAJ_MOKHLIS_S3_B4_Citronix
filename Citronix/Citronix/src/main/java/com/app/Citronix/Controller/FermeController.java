package com.app.Citronix.Controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.DTO.Request.FermeRequest;
import com.app.Citronix.Model.DTO.Response.FermeResponse;
import com.app.Citronix.Service.FermeService;

@RestController
@RequestMapping("/api/fermes")
public class FermeController {
    
    @Autowired
    private FermeService fermeService;

    @PostMapping
public ResponseEntity<FermeResponse> createFerme(@Valid @RequestBody FermeRequest request) throws ResponseException { 
        FermeResponse savedFerme = fermeService.saveFerme(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFerme);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FermeResponse> getFermeById(@PathVariable Long id) {
        FermeResponse ferme = fermeService.getFermeById(id);
        if (ferme == null) {
            throw new ResponseException("Ferme non trouvé avec l'id: " + id, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ferme);
    }

    @GetMapping
    public ResponseEntity<Page<FermeResponse>> getAllFermes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(fermeService.getAllFermes(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FermeResponse> updateFerme(@PathVariable Long id, @Valid @RequestBody FermeRequest request) throws ResponseException {
        
        return ResponseEntity.ok(fermeService.updateFerme(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFerme(@PathVariable Long id) {
        if (!fermeService.deleteFerme(id)) {
            throw new ResponseException("Ferme non trouvé avec l'id: " + id, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<FermeResponse>> searchFermes(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String adress,
            @RequestParam(required = false) Double minSuperficie,
            @RequestParam(required = false) Double maxSuperficie,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(fermeService.searchFermes(nom, adress, minSuperficie, maxSuperficie, startDate, endDate, pageable));
    }


}
