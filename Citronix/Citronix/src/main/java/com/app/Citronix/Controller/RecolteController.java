package com.app.Citronix.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.Citronix.Model.DTO.Request.DetailRecolteRequest;
import com.app.Citronix.Model.DTO.Response.DetailRecolteResponse;
import com.app.Citronix.Model.DTO.Response.RecolteResponse;
import com.app.Citronix.Service.RecolteDetailService;
import com.app.Citronix.Service.RecolteService;

@RestController
@RequestMapping("/api/recoltes")
public class RecolteController {

    @Autowired
    private RecolteService recolteService;

    @Autowired
    private RecolteDetailService recolteDetailService;

    @GetMapping
    public ResponseEntity<Page<RecolteResponse>> getAllRecoltes(Pageable pageable) {
        Page<RecolteResponse> recoltes = recolteService.getAllRecoltes(pageable);
        return ResponseEntity.ok(recoltes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecolteResponse> getRecolteById(@PathVariable Long id) {
        RecolteResponse recolte = recolteService.getRecolteById(id);
        return ResponseEntity.ok(recolte);
    }

    @PostMapping("/createDetailRecolte")
    public ResponseEntity<DetailRecolteResponse> createRecolteDetail(@RequestBody DetailRecolteRequest detailRecolteRequest) {
        DetailRecolteResponse detailRecolte = recolteDetailService.createRecolteDetail(detailRecolteRequest);
        return ResponseEntity.ok(detailRecolte);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<DetailRecolteResponse> getRecolteDetails(@PathVariable Long id) {
        DetailRecolteResponse details = recolteDetailService.getRecolteDetailById(id);
        return ResponseEntity.ok(details);
    }
    @GetMapping("/details")
    public ResponseEntity<Page<DetailRecolteResponse>> getRecolteDetails(Pageable pageable) {
        Page<DetailRecolteResponse> details = recolteDetailService.getAllRecolteDetails(pageable);
        return ResponseEntity.ok(details);
    }

} 