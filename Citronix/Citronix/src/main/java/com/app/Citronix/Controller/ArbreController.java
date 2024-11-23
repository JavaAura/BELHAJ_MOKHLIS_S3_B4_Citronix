package com.app.Citronix.Controller;

import com.app.Citronix.Model.DTO.Request.ArbreRequest;
import com.app.Citronix.Model.DTO.Response.ArbreResponse;
import com.app.Citronix.Service.ArbreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/arbres")
public class ArbreController {
    
    @Autowired
    private ArbreService arbreService;

    @GetMapping
    public Page<ArbreResponse> getAllArbres(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "0") int page) {
        
        Pageable customPageable = PageRequest.of(
            page, 
            size        );
        
        return arbreService.findAll(customPageable);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ArbreResponse> getArbreById(@PathVariable Long id) {
        return arbreService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ArbreResponse createArbre(@RequestBody ArbreRequest arbreRequest) {
        return arbreService.save(arbreRequest);
    }
    @DeleteMapping("/{id}")
    public void deleteArbre(@PathVariable Long id) {
        arbreService.deleteById(id);
    }

}
