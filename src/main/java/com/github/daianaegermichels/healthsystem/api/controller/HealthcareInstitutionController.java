package com.github.daianaegermichels.healthsystem.api.controller;

import com.github.daianaegermichels.healthsystem.dto.HealthcareInstitutionDTO;
import com.github.daianaegermichels.healthsystem.service.HealthcareInstitutionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@Api(value = "API REST Healthcare Institutions")
@RequestMapping("/api/healthcare-institutions")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000", "https://health-system-api.herokuapp.com", "https://health-system-app.netlify.app"})
public class HealthcareInstitutionController {

    private HealthcareInstitutionService healthcareInstitutionService;

    public HealthcareInstitutionController(HealthcareInstitutionService healthcareInstitutionService){
        this.healthcareInstitutionService = healthcareInstitutionService;
    }

    @PostMapping
    @ApiOperation(value = "Create a new Healthcare Institution")
    public ResponseEntity<HealthcareInstitutionDTO> createHealthcareInstitution(@Valid @RequestBody HealthcareInstitutionDTO healthcareInstitutionDTO) {

        healthcareInstitutionService.save(healthcareInstitutionDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(healthcareInstitutionDTO.getId()).toUri();

        return ResponseEntity.created(location).body(healthcareInstitutionDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get an Healthcare Institution by Id")
    public ResponseEntity<Object> getAnHealthcareInstitution(@PathVariable(name= "id") Long id){
        var healthcareInstitution = healthcareInstitutionService.getById(id);
        if(!healthcareInstitution.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(healthcareInstitution.get());
    }

    @PostMapping("/authenticate")
    public ResponseEntity authenticateInstitution(@RequestBody HealthcareInstitutionDTO healthcareInstitutionDTO){
            var institutionAuthenticate = healthcareInstitutionService.authenticateInstitution(healthcareInstitutionDTO);
            return ResponseEntity.ok(institutionAuthenticate);
    }

}
