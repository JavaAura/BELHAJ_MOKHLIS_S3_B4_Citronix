package com.app.Citronix.Controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.Citronix.Model.DTO.FermeDTO;
import com.app.Citronix.Model.View.FermeView;
import com.app.Citronix.Service.FermeService;

@RestController
@RequestMapping("/api/fermes")
public class FermeController {
    
    @Autowired
    private FermeService fermeService;

    @PostMapping
    public ResponseEntity<FermeDTO> creatFerme(@Valid @RequestBody  FermeDTO request) {    	
        return ResponseEntity.ok(fermeService.saveFerme(request));
    }
    
    @GetMapping
    public ResponseEntity<Page<FermeView>> getAllFermes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(fermeService.getAllFermes(pageable));
    }
}
