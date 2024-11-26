package com.app.Citronix.Controller;

import com.app.Citronix.Model.DTO.Request.VenteRequest;
import com.app.Citronix.Model.DTO.Response.VenteResponse;
import com.app.Citronix.Service.VenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ventes")
public class VenteController {
    
    @Autowired
    private VenteService venteService;
    
    @GetMapping
    public List<VenteResponse> getAllVentes() {
        return venteService.getAllVentes();
    }
    
    @GetMapping("/{id}")
        public ResponseEntity<VenteResponse> getVenteById(@PathVariable Long id) {
        return venteService.getVenteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public VenteResponse createVente(@RequestBody VenteRequest venteRequest) {
        return venteService.saveVente(venteRequest);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<VenteResponse> updateVente(@PathVariable Long id, @RequestBody VenteRequest venteRequest) {
        return venteService.updateVente(id, venteRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVente(@PathVariable Long id) {
        return venteService.deleteVente(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
