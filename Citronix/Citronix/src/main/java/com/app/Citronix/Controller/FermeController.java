package com.app.Citronix.Controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.app.Citronix.Model.DTO.Request.FermeRequest;
import com.app.Citronix.Model.DTO.Response.FermeResponse;
import com.app.Citronix.Service.FermeService;

@RestController
@RequestMapping("/api/fermes")
public class FermeController {
    
    @Autowired
    private FermeService fermeService;

    @PostMapping
    public ResponseEntity<FermeResponse> createFerme(@Valid @RequestBody FermeRequest request) {    	
        return ResponseEntity.ok(fermeService.saveFerme(request));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FermeResponse> getFermeById(@PathVariable Long id) {
        FermeResponse ferme = fermeService.getFermeById(id);
        if (ferme == null) {
            throw new RuntimeException("Ferme not found with id: " + id);
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
    public ResponseEntity<FermeResponse> updateFerme(@PathVariable Long id, @Valid @RequestBody FermeRequest request) {
        return ResponseEntity.ok(fermeService.updateFerme(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFerme(@PathVariable Long id) {
        if (!fermeService.deleteFerme(id)) {
            throw new RuntimeException("Ferme not found with id: " + id);
        }else{
            return ResponseEntity.noContent().build();
        }
    }


}
