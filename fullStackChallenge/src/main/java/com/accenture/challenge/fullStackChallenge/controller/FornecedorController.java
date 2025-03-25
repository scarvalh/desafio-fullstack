package com.accenture.challenge.fullStackChallenge.controller;

import com.accenture.challenge.fullStackChallenge.dto.FornecedorDTO;
import com.accenture.challenge.fullStackChallenge.services.FornecedorService;
import jakarta.xml.bind.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedor")

public class FornecedorController {
    private FornecedorService service;

    public FornecedorController(FornecedorService fornecedorService) {
        this.service = fornecedorService;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public ResponseEntity<FornecedorDTO> createFornecedor(@RequestBody FornecedorDTO body) throws ValidationException {
        var resul = service.createFornecedor(body);
        return ResponseEntity.ok(resul);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping
    public ResponseEntity<FornecedorDTO> updateFornecedor(@RequestBody FornecedorDTO body) throws ValidationException {
        var resul = service.updateFornecedor(body);
        return ResponseEntity.ok(resul);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public ResponseEntity<List<FornecedorDTO>> getAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/find-fornecedor-by-id/{id}")
    public ResponseEntity<FornecedorDTO> getById(@PathVariable Integer id){
        return ResponseEntity.ok(service.findById(id));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/delete-by-id/{id}")
    public void deleteById(@PathVariable  Integer id) throws ValidationException {
        service.deleteByID(id);
    }
}
