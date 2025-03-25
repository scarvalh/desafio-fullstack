package com.accenture.challenge.fullStackChallenge.controller;

import com.accenture.challenge.fullStackChallenge.dto.EmpresaDTO;
import com.accenture.challenge.fullStackChallenge.services.EmpresaService;
import jakarta.xml.bind.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresa")
public class EmpresaController {

    private EmpresaService service;

    public EmpresaController(EmpresaService service) {
        this.service = service;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public ResponseEntity<EmpresaDTO> createEmpresa(@RequestBody EmpresaDTO body) throws ValidationException {
        var resul = service.createEmpresa(body);
        return ResponseEntity.ok(resul);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping
    public ResponseEntity<EmpresaDTO> updateEmpresa(@RequestBody EmpresaDTO body) throws ValidationException {
        var resul = service.updateEmpresa(body);
        return ResponseEntity.ok(resul);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> getAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/find-empresa-by-id/{id}")
    public ResponseEntity<EmpresaDTO> getById(@PathVariable Integer id){
     return ResponseEntity.ok(service.findByID(id));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/delete-by-id/{id}")
    public void deleteById(@PathVariable Integer id) throws ValidationException {
        service.deleteById(id);
    }



}

