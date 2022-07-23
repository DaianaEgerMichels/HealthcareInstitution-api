package com.github.daianaegermichels.healthsystem.api.controller;

import com.github.daianaegermichels.healthsystem.model.entity.HealthcareInstitution;
import com.github.daianaegermichels.healthsystem.service.HealthcareInstitutionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@CrossOrigin(origins = "*")
@Api(value = "API REST Healthcare Institutions")
@RequestMapping("/api/healthcare-institutions")
public class HealthcareInstitutionController {

    private HealthcareInstitutionService healthcareInstitutionService;

    public HealthcareInstitutionController(HealthcareInstitutionService healthcareInstitutionService){
        this.healthcareInstitutionService = healthcareInstitutionService;
    }

    @PostMapping
    @ApiOperation(value = "Create a new Healthcare Institution")
    public ResponseEntity<HealthcareInstitution> createHealthcareInstitution(@Valid @RequestBody HealthcareInstitution healthcareInstitution) {

        healthcareInstitutionService.save(healthcareInstitution);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(healthcareInstitution.getId()).toUri();

        return ResponseEntity.created(location).body(healthcareInstitution);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get an Healthcare Institution by Id")
    public ResponseEntity<Object> getAnHealthcareInstitution(@PathVariable(value= "id") Long id){
        var healthcareInstitution = healthcareInstitutionService.getById(id);
        if(!healthcareInstitution.isPresent()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(healthcareInstitution);
    }


}
